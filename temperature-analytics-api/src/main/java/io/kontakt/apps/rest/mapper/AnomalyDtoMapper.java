package io.kontakt.apps.rest.mapper;

import io.kontak.apps.AnomalyEntity;
import io.kontakt.apps.rest.dto.AnomalyDto;

public class AnomalyDtoMapper {

    public static AnomalyDto toAnomalyDto(final AnomalyEntity anomaly) {
        return new AnomalyDto(
                anomaly.temperature(),
                anomaly.roomId(),
                anomaly.thermometerId(),
                anomaly.timestamp());
    }
}
