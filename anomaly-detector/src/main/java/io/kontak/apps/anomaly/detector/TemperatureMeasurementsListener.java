package io.kontak.apps.anomaly.detector;

import io.kontak.apps.anomaly.detector.serder.TemperatureReadingsSerde;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.WindowStore;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class TemperatureMeasurementsListener implements Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> {

    private final AnomalyDetector anomalyDetector;

    private final int TIME_WINDOW = 10;

    public TemperatureMeasurementsListener(AnomalyDetector anomalyDetector) {
        this.anomalyDetector = anomalyDetector;
    }

    @Override
    public KStream<String, Anomaly> apply(KStream<String, TemperatureReading> events) {
        return events
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(TIME_WINDOW)))
                .aggregate(
                        LinkedList::new,
                        (key, value, aggregate) -> {
                            aggregate.add(value);
                            return aggregate;
                        },
                        Materialized.<String, List<TemperatureReading>, WindowStore<Bytes, byte[]>>as("temperature-readings-window-store")
                                .withValueSerde(new TemperatureReadingsSerde())
                )
                .toStream()
                .map((key, value) -> KeyValue.pair(key.key(), value))
                .flatMapValues(aggregate -> {
                    if (aggregate == null) {
                        return Collections.emptyList();
                    }
                    log.info("Aggregation result {}",aggregate.stream().map(Object::toString).collect(Collectors.toSet()));
                    return anomalyDetector.apply(aggregate).map(Collections::singletonList).orElse(Collections.emptyList());
                });

    }

}
