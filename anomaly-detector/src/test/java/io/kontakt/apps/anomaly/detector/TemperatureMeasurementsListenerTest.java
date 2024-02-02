package io.kontakt.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class TemperatureMeasurementsListenerTest extends AbstractIntegrationTest {

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-in-0.destination}")
    private String inputTopic;

    @Value("${spring.cloud.stream.bindings.anomalyDetectorProcessor-out-0.destination}")
    private String outputTopic;

    @Test
    void testInOutFlow() {
        try (TestKafkaConsumer<Anomaly> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                outputTopic,
                Anomaly.class
        );
             TestKafkaProducer<TemperatureReading> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {
            TemperatureReading temperatureReading1 = new TemperatureReading(21d, "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"));
            TemperatureReading temperatureReading2 = new TemperatureReading(22d, "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"));
            TemperatureReading temperatureReading3 = new TemperatureReading(23d, "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"));
            TemperatureReading temperatureReading4 = new TemperatureReading(20d, "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"));
            TemperatureReading temperatureReading5 = new TemperatureReading(30d, "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"));
            TemperatureReading temperatureReading6 = new TemperatureReading(19d, "room", "thermometer", Instant.parse("2023-01-01T00:00:00.000Z"));
            List<TemperatureReading> list = new ArrayList<>();
            list.add(temperatureReading1);
            list.add(temperatureReading2);
            list.add(temperatureReading3);
            list.add(temperatureReading4);
            list.add(temperatureReading5);
            list.add(temperatureReading6);

            list.stream().forEach(temperatureReading-> {
                producer.produce(temperatureReading.roomId(), temperatureReading);
            });

            consumer.drain(
                    consumerRecords -> consumerRecords.stream().anyMatch(r -> r.value().thermometerId().equals(temperatureReading1.thermometerId())),
                    Duration.ofSeconds(11)
            );
        }
    }




}
