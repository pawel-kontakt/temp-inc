package io.kontakt.apps.rest;

import io.kontak.apps.AnomalyEntity;

import java.math.BigDecimal;
import java.time.Instant;

public class AnomalyEntityFixture {
    public static AnomalyEntity getAnyAnomalyEntity() {
        return new AnomalyEntity("123", "1", BigDecimal.valueOf(20.5), Instant.now());
    }
}
