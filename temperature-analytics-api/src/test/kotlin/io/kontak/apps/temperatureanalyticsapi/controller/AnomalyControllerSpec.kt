package io.kontak.apps.temperatureanalyticsapi.controller

import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.Thermometer
import io.kontak.apps.temperatureanalyticsapi.service.AnomalyService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*

@WebFluxTest(controllers = [AnomalyController::class])
class AnomalyControllerSpec {

    @Autowired
    private lateinit var webTestClient: WebTestClient


    @MockBean
    private lateinit var anomalyService: AnomalyService

    private val random = Random()


    @Test
    fun `test getAnomaliesByRoomId`() {
        val roomId = "room1"
        Mockito.`when`(anomalyService.findAnomaliesByRoomId(roomId))
            .thenReturn(Flux.just(Anomaly(random.nextDouble(40.0), "room1","thermo1", Instant.now())))

        webTestClient.get().uri("/anomalies/room/$roomId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Anomaly::class.java)
    }

    @Test
    fun `test getAnomaliesByThermometerId`() {
        val thermometerId = "thermo1"

        Mockito.`when`(anomalyService.findAnomaliesByThermometerId(thermometerId))
            .thenReturn(Flux.just(Anomaly(random.nextDouble(40.0), "room1","thermo1", Instant.now())))

        webTestClient.get().uri("/anomalies/thermometer/$thermometerId")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].thermometerId").isEqualTo(thermometerId)
    }


    @Test
    fun `test getThermometersWithAnomaliesAboveThreshold`() {
        Mockito.`when`(anomalyService.findThermometersWithAnomaliesAboveThreshold(1))
            .thenReturn(Flux.just(Thermometer("thermo1", 2)))

        webTestClient.get().uri("/thermometers/anomalies?threshold=1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$[0].thermometerId").isEqualTo("thermo1")
            .jsonPath("$[0].anomalyCount").isEqualTo(2)
    }

}
