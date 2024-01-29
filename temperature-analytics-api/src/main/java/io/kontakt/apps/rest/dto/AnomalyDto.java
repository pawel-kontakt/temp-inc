package io.kontakt.apps.rest.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record AnomalyDto(BigDecimal temperature, String roomId, String thermometerId, Instant timestamp) {

}