package io.kontak.apps.anomaly.detector.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.kontak.apps.anomaly.detector.AnomalyDetector;
import io.kontak.apps.anomaly.detector.TemperatureMeasurementsListener;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimeZone;
import java.util.function.Function;

@Configuration
public class KafkaConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true)
                .defaultDateFormat(new StdDateFormat().withTimeZone(TimeZone.getTimeZone("UTC"))
                        .withColonInTimeZone(false))
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }

    @Bean
    public Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> anomalyDetectorProcessor(
            AnomalyDetector anomalyDetector,
            @Value("${anomaly.detector.state.store.name}") String stateStoreName) {
        return new TemperatureMeasurementsListener(anomalyDetector, stateStoreName);
    }

    @Bean
    public StoreBuilder myStore(
            ObjectMapper objectMapper,
            @Value("${anomaly.detector.state.store.name}") String stateStoreName) {
        return Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(stateStoreName), Serdes.String(),
                Serdes.ListSerde(ArrayList.class, Serdes.serdeFrom(
                        new TemperatureReadingSerializer(objectMapper),
                        new TemperatureReadingDeserializer(objectMapper)
                )));
    }

    private static class TemperatureReadingSerializer implements Serializer<TemperatureReading> {

        private final ObjectMapper objectMapper;

        private TemperatureReadingSerializer(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public byte[] serialize(String topic, TemperatureReading data) {
            if (data == null) {
                return null;
            }

            try {
                return objectMapper.writeValueAsBytes(data);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class TemperatureReadingDeserializer implements Deserializer<TemperatureReading> {

        private final ObjectMapper objectMapper;

        private TemperatureReadingDeserializer(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public TemperatureReading deserialize(String topic, byte[] bytes) {
            if (bytes == null) {
                return null;
            }

            try {
                return objectMapper.readValue(bytes, TemperatureReading.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
