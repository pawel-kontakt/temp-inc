package io.kontak.apps.temperatureanalyticsapi.repository

import io.kontak.apps.event.Anomaly
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import reactor.test.StepVerifier
import java.time.Instant
import java.util.*

@DataMongoTest
class AnomalyRepositorySpec(@Autowired val anomalyRepository: AnomalyRepository) {

    private val random = Random()


    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri") { "mongodb://localhost:27017/testdb" }
        }
    }

    @BeforeEach
    fun setup() {
        anomalyRepository.deleteAll().block()
    }


    @Test
    fun `test findByRoomId`() {
        val roomAnomalies = listOf(
            Anomaly(random.nextDouble(40.0), "room2", "thermo2", Instant.now()),
            Anomaly(random.nextDouble(40.0), "room2", "thermo3", Instant.now())
        )

        roomAnomalies.forEach { anomalyRepository.save(it).block() }

        val found = anomalyRepository.findByRoomId("room2")

        StepVerifier.create(found)
            .expectNextMatches { it.roomId == "room2" }
            .expectNextMatches { it.roomId == "room2" }
            .verifyComplete()
    }
    @Test
    fun `test findByThermometerId`() {
        val anomaly = Anomaly(random.nextDouble(40.0), "room5","thermo5", Instant.now())
        anomalyRepository.save(anomaly).block()

        val found = anomalyRepository.findByThermometerId("thermo5")

        StepVerifier.create(found)
            .expectNextMatches { it.thermometerId == "thermo5" }
            .verifyComplete()
    }

    @Test
    fun `test countByThermometerIdGreaterThanThreshold`() {
        val anomalies = listOf(
            Anomaly(random.nextDouble(40.0), "room3", "thermo4", Instant.now()),
            Anomaly(random.nextDouble(40.0), "room3", "thermo4", Instant.now())
        )

        anomalies.forEach { anomalyRepository.save(it).block() }

        val threshold = 1
        val countFlux = anomalyRepository.findByThermometerId("thermo4")
            .count()
            .filter { count -> count > threshold }

        StepVerifier.create(countFlux)
            .expectNextCount(1)
            .verifyComplete()
    }




}