package io.kontakt.apps.rest.mapper;

import io.kontak.apps.AnomalyEntity;
import io.kontakt.apps.rest.dto.AnomalyThermometerIdDto;
import org.junit.jupiter.api.Test;

import static io.kontakt.apps.rest.AnomalyEntityFixture.getAnyAnomalyEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnomalyThermometerIdDtoMapperTest {

    @Test
    void anomalyEntity_is_mapped_to_AnomalyThermometerIdDto() {
        //given
        final AnomalyEntity anomalyToMap = getAnyAnomalyEntity();
        //when
        final AnomalyThermometerIdDto result = AnomalyThermometerIdDtoMapper.toAnomalyThermometerIdDto(anomalyToMap);

        //then
        assertEquals(anomalyToMap.thermometerId(), result.thermometerId());
    }
}