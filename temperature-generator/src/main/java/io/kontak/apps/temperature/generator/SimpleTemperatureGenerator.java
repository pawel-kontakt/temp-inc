package io.kontak.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class SimpleTemperatureGenerator implements TemperatureGenerator {

  private final Random random = new Random();

  @Override
  public List<TemperatureReading> generate() {
    List<TemperatureReading> generated = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      generated.add(generateSingleReading());
    }

    return generated;
  }

  private TemperatureReading generateSingleReading() {
    //TODO basic implementation, should be changed to the one that will allow to test and demo solution on realistic data
    return new TemperatureReading(
        random.nextDouble(10d, 30d),
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        Instant.now()
    );
  }
}
