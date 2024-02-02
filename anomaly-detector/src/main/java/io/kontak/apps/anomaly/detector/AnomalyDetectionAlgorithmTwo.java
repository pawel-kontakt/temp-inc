package io.kontak.apps.anomaly.detector;


import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AnomalyDetectionAlgorithmTwo implements AnomalyDetector {

    @Override
    public Optional<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        if (temperatureReadings.isEmpty()) {
            return Optional.empty();
        }

        double average = temperatureReadings.stream()
                .mapToDouble(TemperatureReading::temperature)
                .average()
                .orElse(Double.NaN);

        return temperatureReadings.stream()
                .filter(reading -> Math.abs(reading.temperature() - average) > 5)
                .findFirst()
                .map(reading -> new Anomaly(
                        reading.temperature(),
                        reading.roomId(),
                        reading.thermometerId(),
                        reading.timestamp()
                ));
    }
}
