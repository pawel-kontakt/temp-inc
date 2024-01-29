package io.kontakt.apps.config;

import io.kontak.apps.PlaceId;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.usecase.AnomalyDetected;
import io.kontakt.apps.listener.AnomaliesListener;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.function.Function;

@Configuration
public class KafkaConfig {

    @Bean
    public Function<KStream<String, Anomaly>, KStream<String, List<PlaceId>>> anomalyStorageProcessor(AnomalyDetected anomalyDetected) {
        return new AnomaliesListener(anomalyDetected);
    }

}
