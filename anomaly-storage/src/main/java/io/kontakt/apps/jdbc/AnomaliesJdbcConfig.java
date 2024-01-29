package io.kontakt.apps.jdbc;

import io.kontak.apps.usecase.AnomalyDetected;
import io.kontak.apps.usecase.FindAnomalies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AnomaliesJdbcConfig {

    @Bean
    FindAnomalies findAnomalies(JdbcTemplate jdbcTemplate) {
        return new FindAnomaliesJdbc(jdbcTemplate);
    }

    @Bean
    AnomalyDetected anomalyDetected(JdbcTemplate jdbcTemplate) {
        return new AnomalyDetectedJdbc(jdbcTemplate);
    }
}