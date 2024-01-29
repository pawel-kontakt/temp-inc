package io.kontakt.apps.rest;

import io.kontak.apps.Anomalies;
import io.kontak.apps.AnomalyEntity;
import io.kontak.apps.usecase.FindAnomalies;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static io.kontakt.apps.rest.AnomalyEntityFixture.getAnyAnomalyEntity;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(AnomalyController.class)
class AnomalyControllerTest {

    @MockBean
    private FindAnomalies findAnomalies;

    @MockBean
    private Anomalies anomalies;

    private AnomalyController anomalyController;

    @Value("${temperature.default.threshold}")
    private BigDecimal defaultThreshold;

    @Autowired
    private MockMvc mockMvc;

    final AnomalyEntity expectedAnomaly = getAnyAnomalyEntity();


    @Test
    public void anomalies_are_returned_by_thermometerId() throws Exception {
        mockAnomaliesJdbc();

        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/anomalies/thermometer/{thermometerId}", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        final String stringResult = result.getResponse().getContentAsString();

        assertTrue(stringResult.contains(expectedAnomaly.roomId()));
        assertTrue(stringResult.contains(expectedAnomaly.thermometerId()));
        assertTrue(stringResult.contains(expectedAnomaly.temperature().toString()));
    }

    @Test
    public void anomalies_are_returned_by_roomId() throws Exception {
        mockAnomaliesJdbc();

        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/anomalies/room/{roomId}", "123")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        final String stringResult = result.getResponse().getContentAsString();

        assertTrue(stringResult.contains(expectedAnomaly.roomId()));
        assertTrue(stringResult.contains(expectedAnomaly.thermometerId()));
        assertTrue(stringResult.contains(expectedAnomaly.temperature().toString()));
    }


    @Test
    public void thermometerIds_are_returned_by_default_threshold() throws Exception {
        mockAnomaliesJdbc();

        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/anomalies/thermometers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        final String stringResult = result.getResponse().getContentAsString();

        assertTrue(stringResult.contains(expectedAnomaly.thermometerId()));
    }

    @Test
    public void thermometerIds_are_returned_by_given_threshold() throws Exception {
        mockAnomaliesJdbc();

        final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/anomalies/thermometers").param("threshold", "20.0")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        final String stringResult = result.getResponse().getContentAsString();

        assertTrue(stringResult.contains(expectedAnomaly.thermometerId()));
        assertFalse(stringResult.contains(expectedAnomaly.roomId()));
    }

    private void mockAnomaliesJdbc() {
        when(findAnomalies.all()).thenReturn(anomalies);
        when(anomalies.filterBy(any())).thenReturn(anomalies);
        when(anomalies.stream()).thenReturn(Stream.of(expectedAnomaly));
    }

}
