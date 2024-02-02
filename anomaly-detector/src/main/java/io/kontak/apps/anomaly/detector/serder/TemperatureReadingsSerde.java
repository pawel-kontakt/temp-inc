package io.kontak.apps.anomaly.detector.serder;
import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.common.serialization.Serdes;

import java.util.List;

public class TemperatureReadingsSerde extends Serdes.WrapperSerde<List<TemperatureReading>> {
    public TemperatureReadingsSerde() {
        super(new TemperatureReadingsSerializer(), new TemperatureReadingsDeserializer());
    }
}