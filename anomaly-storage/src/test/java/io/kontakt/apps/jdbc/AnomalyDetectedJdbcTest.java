package io.kontakt.apps.jdbc;

import io.kontak.apps.AnomalyEntity;
import io.kontak.apps.usecase.AnomalyDetected;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.Instant;

@JdbcTest
@ContextConfiguration(classes = AnomaliesJdbcConfig.class)
class AnomalyDetectedJdbcTest {

    @Autowired
    private AnomalyDetected anomalyDetected;

    @Test
    void anomaly_is_persisted() {
        var anomaly = new AnomalyEntity("room_1", "thermometer_id_1", BigDecimal.valueOf(20.0), Instant.now());

        anomalyDetected.anomalyDetected(anomaly);
    }
}