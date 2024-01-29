package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;

import java.util.List;
import java.util.Optional;

public class AlwaysAnomalyAnomalyDetector implements AnomalyDetector {

    private static final int WINDOW_SIZE = 1;

    @Override
    public Optional<Anomaly> apply(List<TemperatureReading> temperatureReadings) {

        TemperatureReading temperatureReading = temperatureReadings.get(0);
        return Optional.of(new Anomaly(
                temperatureReading.temperature(),
                temperatureReading.roomId(),
                temperatureReading.thermometerId(),
                temperatureReading.timestamp()
        ));
    }

    @Override
    public Integer windowSize() {
        return WINDOW_SIZE;
    }
}
