package io.kontak.apps.anomaly.detector.serder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.List;

public class TemperatureReadingsDeserializer implements Deserializer<List<TemperatureReading>> {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public List<TemperatureReading> deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.readValue(data, new TypeReference<List<TemperatureReading>>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Error deserializing TemperatureReading list", e);
        }
    }
}
