package io.kontak.apps.temperature.generator.config;

import io.kontak.apps.event.TemperatureReading;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class RoomPartitionKeyExtractor implements PartitionKeyExtractorStrategy {
    @Override
    public Object extractKey(Message<?> message) {
        TemperatureReading reading = (TemperatureReading) message.getPayload();
        return reading.roomId();
    }

}

