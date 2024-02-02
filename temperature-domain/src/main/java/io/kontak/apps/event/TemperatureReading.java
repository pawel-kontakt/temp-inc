package io.kontak.apps.event;

import java.time.Instant;

public record TemperatureReading(double temperature, String roomId, String thermometerId, Instant timestamp) {

    @Override
    public String toString() {
        return String.format("%s: - %s С'",roomId,temperature);
    }
}
