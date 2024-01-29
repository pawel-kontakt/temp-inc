package io.kontakt.apps.jdbc;

import io.kontak.apps.AnomalyEntity;
import io.kontak.apps.usecase.AnomalyDetected;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class AnomalyDetectedJdbc implements AnomalyDetected {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AnomalyDetectedJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void anomalyDetected(AnomalyEntity anomaly) {
        // UUID.randomUUID() as id is not a good idea, but for the sake of simplicity
        // Better approach is to use a sequence or TimeUUID.
        jdbcTemplate.update(
                "INSERT INTO anomalies(id,room_id,thermometer_id,temperature, anomaly_time) VALUES (?,?, ?, ?, ?)",
                UUID.randomUUID().toString(), anomaly.roomId(), anomaly.thermometerId(), anomaly.temperature(),
                anomaly.timestamp());

    }
}