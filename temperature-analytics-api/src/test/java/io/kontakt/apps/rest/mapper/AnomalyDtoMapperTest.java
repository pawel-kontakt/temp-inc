package io.kontakt.apps.rest.mapper;

import io.kontak.apps.AnomalyEntity;
import io.kontakt.apps.rest.dto.AnomalyDto;
import org.junit.jupiter.api.Test;

import static io.kontakt.apps.rest.AnomalyEntityFixture.getAnyAnomalyEntity;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AnomalyDtoMapperTest {

    @Test
    void anomalyEntity_is_mapped_to_AnomalyDto() {
        //given
        final AnomalyEntity anomalyToMap = getAnyAnomalyEntity();

        //when
        final AnomalyDto result = AnomalyDtoMapper.toAnomalyDto(anomalyToMap);

        //then
        assertAll("Verify all properties",
                () -> assertEquals(anomalyToMap.temperature(), result.temperature()),
                () -> assertEquals(anomalyToMap.roomId(), result.roomId()),
                () -> assertEquals(anomalyToMap.thermometerId(), result.thermometerId()),
                () -> assertEquals(anomalyToMap.timestamp(), result.timestamp()));
    }
}