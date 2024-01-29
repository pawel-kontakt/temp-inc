package io.kontak.apps;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Anomaly entity
 */
public record AnomalyEntity(String roomId, String thermometerId, BigDecimal temperature, Instant timestamp) {
}
