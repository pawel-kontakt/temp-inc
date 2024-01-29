package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class AnomalyInCountBasedWindowDetector implements AnomalyDetector {

    private static final int THRESHOLD = 5;
    private static final int WINDOW_SIZE = 10;

    @Override
    public Optional<Anomaly> apply(List<TemperatureReading> temperatureReadings) {

        if (temperatureReadings.size() < WINDOW_SIZE) {
            return Optional.empty();
        }
        double sum = sumElementsForThreshold(temperatureReadings);

        for (int i = WINDOW_SIZE - 1; i < temperatureReadings.size(); i++) {
            sum += temperatureReadings.get(i).temperature();
            double average = sum / WINDOW_SIZE;

            if (temperatureReadings.get(i).temperature() > average + THRESHOLD) {
                return Optional.of(new Anomaly(
                        temperatureReadings.get(i).temperature(),
                        temperatureReadings.get(i).roomId(),
                        temperatureReadings.get(i).thermometerId(),
                        temperatureReadings.get(i).timestamp()
                ));
            }

            sum -= temperatureReadings.get(i - WINDOW_SIZE + 1).temperature();
        }
        return Optional.empty();
    }

    private double sumElementsForThreshold(final List<TemperatureReading> temperatureReadings) {
        return temperatureReadings.subList(0, WINDOW_SIZE - 1)
                .stream()
                .mapToDouble(TemperatureReading::temperature)
                .sum();
    }

    @Override
    public Integer windowSize() {
        return WINDOW_SIZE;
    }
}