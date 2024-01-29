package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static io.kontak.apps.anomaly.detector.TemperatureReadingFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

class AnomalyInCountBasedWindowDetectorTest {

    final AnomalyDetector detector = new AnomalyInCountBasedWindowDetector();

    @Test
    void return_detected_anomaly() {
        final var expectedAnomaly = new Anomaly(27.1, "123", "12", Instant.now());
        final var temperatureReadingWithAnomaly = createTemperatureReadingWithExpectedDataFromAnomaly(expectedAnomaly);
        var temperatureList = randomTemperatureReadingWithExpectedAnomaly(temperatureReadingWithAnomaly);

        assertThat(detector.apply(temperatureList))
                .isPresent()
                .hasValue(expectedAnomaly);
    }

    @Test
    void return_empty_for_readings_without_anomalies() {
        final var temperatureReadingWithAnomaly = readingsWithoutAnomaly();

        assertThat(detector.apply(temperatureReadingWithAnomaly))
                .isNotPresent();
    }

    @Test
    void return_empty_for_empty_temperature_readings() {
        assertThat(detector.apply(List.of()))
                .isEmpty();
    }
}