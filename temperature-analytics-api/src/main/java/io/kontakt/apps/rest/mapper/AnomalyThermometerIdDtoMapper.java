package io.kontakt.apps.rest.mapper;

import io.kontak.apps.AnomalyEntity;
import io.kontakt.apps.rest.dto.AnomalyThermometerIdDto;

public class AnomalyThermometerIdDtoMapper {

    public static AnomalyThermometerIdDto toAnomalyThermometerIdDto(final AnomalyEntity anomaly) {
        return new AnomalyThermometerIdDto(anomaly.thermometerId());
    }
}