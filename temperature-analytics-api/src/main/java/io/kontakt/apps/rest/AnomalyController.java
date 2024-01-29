package io.kontakt.apps.rest;

import io.kontak.apps.Filter;
import io.kontak.apps.usecase.FindAnomalies;
import io.kontakt.apps.rest.dto.AnomalyResponse;
import io.kontakt.apps.rest.dto.AnomalyThermometersResponse;
import io.kontakt.apps.rest.mapper.AnomalyDtoMapper;
import io.kontakt.apps.rest.mapper.AnomalyThermometerIdDtoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
public class AnomalyController {

    private final FindAnomalies findAnomalies;

    @Value("${temperature.default.threshold}")
    private BigDecimal defaultThreshold;

    public AnomalyController(FindAnomalies findAnomalies) {
        this.findAnomalies = findAnomalies;
    }

    @GetMapping("/anomalies/thermometer/{thermometerId}")
    public AnomalyResponse getAnomaliesByThermometerId(@PathVariable final String thermometerId) {
        final var anomalies = findAnomalies.all()
                .filterBy(
                        Filter.builder().thermometerId(thermometerId).build())
                .stream().
                map(AnomalyDtoMapper::toAnomalyDto).toList();

        return new AnomalyResponse(anomalies);
    }

    @GetMapping("/anomalies/room/{roomId}")
    public AnomalyResponse getAnomaliesByRoomId(@PathVariable final String roomId) {
        final var anomalies = findAnomalies.all()
                .filterBy(
                        Filter.builder().roomId(roomId).build())
                .stream().
                map(AnomalyDtoMapper::toAnomalyDto).toList();
        return new AnomalyResponse(anomalies);
    }

    // if statement is present because of requirement getting threshold from properties
    // otherwise we could use in @RequestParam param defaultValue.
    @GetMapping("/anomalies/thermometers")
    public AnomalyThermometersResponse getThermometersWithAnomalies(
            @RequestParam(name = "threshold") final Optional<BigDecimal> threshold) {
        BigDecimal thresholdToUse = defaultThreshold;

        if (threshold.isPresent()) {
            thresholdToUse = threshold.get();
        }

        final var thermometerIds = findAnomalies.all()
                .filterBy(
                        Filter.builder().threshold(thresholdToUse).build())
                .stream().
                map(AnomalyThermometerIdDtoMapper::toAnomalyThermometerIdDto).toList();

        return new AnomalyThermometersResponse(thermometerIds);
    }
}
