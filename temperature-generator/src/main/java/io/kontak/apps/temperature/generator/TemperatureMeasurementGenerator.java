package io.kontak.apps.temperature.generator;


import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


import io.kontak.apps.event.TemperatureReading;
import org.springframework.stereotype.Component;

@Component
public class TemperatureMeasurementGenerator implements TemperatureGenerator {

    private final Random random = new Random();
    private static final int NUMBER_OF_THERMOMETERS_PER_ROOM = 4;
    private static final int NUMBER_OF_ROOM = 10;

    @Override
    public List<TemperatureReading> generate() {
        return IntStream.range(0, NUMBER_OF_ROOM)
                .boxed()
                .flatMap(roomId -> generateReadingsForRoom("Room-" + roomId))
                .collect(Collectors.toList());
    }

    private Stream<TemperatureReading> generateReadingsForRoom(String roomId) {
        return IntStream.range(0, NUMBER_OF_THERMOMETERS_PER_ROOM)
                .mapToObj(thermometerId -> generateSingleReading(roomId, "Thermometer-" + roomId + "-" + thermometerId));
    }

    private TemperatureReading generateSingleReading(String roomId, String thermometerId) {
        double temperature = generateRandomTemperatureBasedOnTime();
        return new TemperatureReading(
                temperature,
                roomId,
                thermometerId,
                Instant.now()
        );
    }

    private double generateRandomTemperatureBasedOnTime() {
        LocalTime now = LocalTime.now();
        double baseTemp;
        if (now.isAfter(LocalTime.of(6, 0)) && now.isBefore(LocalTime.of(12, 0))) {
            // Morning
            baseTemp = random.nextDouble(15d, 25d);
        } else if (now.isAfter(LocalTime.of(12, 0)) && now.isBefore(LocalTime.of(18, 0))) {
            // Afternoon
            baseTemp = random.nextDouble(20d, 30d);
        } else {
            // Evening and Night
            baseTemp = random.nextDouble(10d, 20d);
        }
        // Adding random fluctuation
        return baseTemp + random.nextGaussian();
    }
}