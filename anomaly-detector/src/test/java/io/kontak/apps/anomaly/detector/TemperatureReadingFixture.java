package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TemperatureReadingFixture {

    static List<TemperatureReading> randomTemperatureReadingWithExpectedAnomaly(final TemperatureReading temperatureReadingWithAnomaly) {
        var readings = readingsWithoutAnomaly();
        List<TemperatureReading> readingsWithAnomaly = new ArrayList<>(readings);
        readingsWithAnomaly.add(temperatureReadingWithAnomaly);
        return readingsWithAnomaly;
    }

    static List<TemperatureReading> readingsWithoutAnomaly() {
        return List.of(
                new TemperatureReading(20.1, "1", "1", Instant.ofEpochMilli(1684945005)),
                new TemperatureReading(21.2, "1", "1", Instant.ofEpochMilli(1684945006)),
                new TemperatureReading(20.3, "1", "3", Instant.ofEpochMilli(1684945007)),
                new TemperatureReading(19.1, "2", "3", Instant.ofEpochMilli(1684945008)),
                new TemperatureReading(20.1, "2", "6", Instant.ofEpochMilli(1684945009)),
                new TemperatureReading(19.2, "3", "5", Instant.ofEpochMilli(1684945010)),
                new TemperatureReading(20.1, "3", "1", Instant.ofEpochMilli(1684945011)),
                new TemperatureReading(18.1, "4", "6", Instant.ofEpochMilli(1684945012)),
                new TemperatureReading(19.4, "4", "6", Instant.ofEpochMilli(1684945013)),
                new TemperatureReading(20.1, "5", "6", Instant.ofEpochMilli(1684945014)),
                new TemperatureReading(23.1, "6", "6", Instant.ofEpochMilli(1684945015)));
    }

    static TemperatureReading createTemperatureReadingWithExpectedDataFromAnomaly(final Anomaly expectedAnomaly) {
        return new TemperatureReading(expectedAnomaly.temperature(), expectedAnomaly.roomId(), expectedAnomaly.thermometerId(), expectedAnomaly.timestamp());
    }

}
