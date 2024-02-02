package io.kontak.apps.temperatureanalyticsapi.service

import io.kontak.apps.event.Anomaly
import io.kontak.apps.temperatureanalyticsapi.repository.AnomalyRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Instant
import java.util.*

@SpringBootTest
class AnomalyServiceSpec {

    @Autowired
    private lateinit var anomalyService: AnomalyService

    @MockBean
    private lateinit var anomalyRepository: AnomalyRepository

    private val random = Random()

    @Test
    fun `test findAnomaliesByRoomId`() {
        val roomId = "room1"
        Mockito.`when`(anomalyRepository.findByRoomId(roomId))
            .thenReturn(Flux.just(Anomaly(random.nextDouble(40.0), "room1","thermo1", Instant.now())))

        val anomalies = anomalyService.findAnomaliesByRoomId(roomId)

        StepVerifier.create(anomalies)
            .expectNextMatches { it.roomId == roomId }
            .verifyComplete()
    }


    @Test
    fun `test findAnomaliesByThermometerId`() {
        val thermometerId = "thermo1"
        Mockito.`when`(anomalyRepository.findByThermometerId(thermometerId))
            .thenReturn(Flux.just(Anomaly(random.nextDouble(40.0), "room1","thermo1", Instant.now())))

        val anomalies = anomalyService.findAnomaliesByThermometerId(thermometerId)

        StepVerifier.create(anomalies)
            .expectNextMatches { it.thermometerId == thermometerId }
            .verifyComplete()
    }

    @Test
    fun `test findThermometersWithAnomaliesAboveThreshold`() {
        Mockito.`when`(anomalyRepository.findAll())
            .thenReturn(Flux.just(
                Anomaly(random.nextDouble(40.0), "room1","thermo1", Instant.now()),
                Anomaly(random.nextDouble(40.0), "room2","thermo1", Instant.now()),
                Anomaly(random.nextDouble(40.0), "room1","thermo2", Instant.now())
        ))

        val threshold = 1
        val thermometers = anomalyService.findThermometersWithAnomaliesAboveThreshold(threshold)

        StepVerifier.create(thermometers)
            .expectNextMatches { it.thermometerId == "thermo1" && it.anomalyCount > threshold }
            .verifyComplete()
    }


}