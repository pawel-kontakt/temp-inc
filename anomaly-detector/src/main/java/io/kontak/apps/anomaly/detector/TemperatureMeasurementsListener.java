package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.streams.kstream.KStream;

import java.util.function.Function;

public class TemperatureMeasurementsListener implements Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> {

    private final AnomalyDetector anomalyDetector;
    private final String stateStoreName;

    public TemperatureMeasurementsListener(AnomalyDetector anomalyDetector, String stateStoreName) {
        this.anomalyDetector = anomalyDetector;
        this.stateStoreName = stateStoreName;
    }

    @Override
    public KStream<String, Anomaly> apply(KStream<String, TemperatureReading> events) {
        return events
                .transform(
                        () -> new AnomalyDetectorTransformer(anomalyDetector.windowSize(), stateStoreName),
                        stateStoreName)
                .mapValues(anomalyDetector::apply)
                .filter((s, anomaly) -> anomaly.isPresent())
                .mapValues((s, anomaly) -> anomaly.get())
                .selectKey((s, anomaly) -> anomaly.thermometerId());

    }
}
