package io.kontak.apps.anomaly.detector.serder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.common.serialization.Serializer;

import java.util.List;

public class TemperatureReadingsSerializer implements Serializer<List<TemperatureReading>> {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());


    @Override
    public byte[] serialize(String topic, List<TemperatureReading> data) {
        if (data == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new IllegalStateException("Error serializing TemperatureReading list", e);
        }
    }

}
