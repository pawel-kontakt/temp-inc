package io.kontak.apps.anomaly.detector.config;

import io.kontak.apps.anomaly.detector.AlwaysAnomalyAnomalyDetector;
import io.kontak.apps.anomaly.detector.AnomalyDetector;
import io.kontak.apps.anomaly.detector.AnomalyInCountBasedWindowDetector;
import io.kontak.apps.anomaly.detector.AnomalyInTimeBasedWindowDetector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AnomalyDetectorConfiguration {

    @Value("${anomaly.detector.algorithm.version}")
    private int algorithmVersion;

    @Bean
    @Primary
    @ConditionalOnProperty(name = "anomaly.detector.algorithm.version", havingValue = "0")
    public AnomalyDetector alwaysAnomalyAnomalyDetector() {
        return new AlwaysAnomalyAnomalyDetector();
    }

    @Bean
    @ConditionalOnProperty(name = "anomaly.detector.algorithm.version", havingValue = "1")
    public AnomalyDetector AnomalyInCountBasedWindowDetector() {
        return new AnomalyInCountBasedWindowDetector();
    }

    @Bean
    @ConditionalOnProperty(name = "anomaly.detector.algorithm.version", havingValue = "2")
    public AnomalyDetector AnomalyInTimeBasedWindowDetector() {
        return new AnomalyInTimeBasedWindowDetector();
    }
}