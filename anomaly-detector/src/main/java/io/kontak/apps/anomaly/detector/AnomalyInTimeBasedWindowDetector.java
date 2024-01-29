package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AnomalyInTimeBasedWindowDetector implements AnomalyDetector {
    private static final int DEVIATION = 5;

    @Override
    public Optional<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        final var windowSize = Duration.ofSeconds(windowSize());
        if (temperatureReadings.isEmpty()) {
            return Optional.empty();
        }

        Queue<TemperatureReading> window = new ConcurrentLinkedQueue<>();
        double sum = 0;

        for (TemperatureReading reading : temperatureReadings) {
            Instant currentTimestamp = reading.timestamp();

            while (!window.isEmpty() && window.peek().timestamp().isBefore(currentTimestamp.minus(windowSize))) {
                TemperatureReading removedReading = window.poll();
                sum -= removedReading.temperature();
            }

            window.offer(reading);
            sum += reading.temperature();

            double windowAverage = sum / window.size();

            if (Math.abs(reading.temperature() - windowAverage) >= DEVIATION) {
                return Optional.of(new Anomaly(reading.temperature(), reading.roomId(), reading.thermometerId(), reading.timestamp()));
            }
        }

        return Optional.empty();
    }

    @Override
    public Integer windowSize() {
        return 10;
    }
}
