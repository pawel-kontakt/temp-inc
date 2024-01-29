package io.kontak.apps.temperature.generator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.kontak.apps.PlaceId;
import io.kontak.apps.event.TemperatureReading;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Primary
public class WeatherApiGenerator implements TemperatureGenerator {

  private static final Logger logger = LoggerFactory.getLogger(WeatherApiGenerator.class);

  private static final List<PlaceId> PLACES = Arrays.asList(
      new PlaceId("Wroclaw", "thermometer1"),
      new PlaceId("Wroclaw", "thermometer2"),
      new PlaceId("Warszawa", "thermometer1"),
      new PlaceId("Warszawa", "thermometer2"),
      new PlaceId("Krakow", "thermometer1"),
      new PlaceId("Krakow", "thermometer2"),
      new PlaceId("Gdansk", "thermometer1"),
      new PlaceId("Gdansk", "thermometer2")
  );
  private final RestTemplate restTemplate;
  private final UriComponentsBuilder uriComponents;

  public WeatherApiGenerator(RestTemplate restTemplate,
      @Value("${temperature-generator.wether-api.url}") String apiUrl,
      @Value("${temperature-generator.wether-api.key}") String apiKey) {
    this.restTemplate = restTemplate;

    uriComponents = UriComponentsBuilder
        .fromUriString(apiUrl)
        .queryParam("key", apiKey);
  }

  private static PlaceId randomPlace() {
    return PLACES.get((int) (Math.random() * PLACES.size()));
  }

  @Override
  public List<TemperatureReading> generate() {
    var place = randomPlace();

    var weatherApiUrl = uriComponents
        .queryParam("q", place.roomId())
        .queryParam("aqi", "no")
        .build().toUri();

    var current = restTemplate.getForEntity(weatherApiUrl, WeatherApiResponse.class).getBody()
        .current();

    logger.info("Generated temperature reading: {} for place {}", current, place);

    return List.of(
        new TemperatureReading(current.temperature, place.roomId(), place.thermometerId(),
            // According to www.weatherapi.com documentation, the last updated time is in UTC
            current.lastUpdated().toInstant(ZoneOffset.UTC)));
  }

  record WeatherApiResponse(WeatherApiReading current) {

  }

  record WeatherApiReading(@JsonProperty("temp_c") double temperature,
                           @JsonProperty("last_updated")
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm") LocalDateTime lastUpdated) {

  }

}
