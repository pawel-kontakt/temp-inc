package io.kontak.apps.temperatureanalyticsapi.service

import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.Thermometer
import io.kontak.apps.temperatureanalyticsapi.repository.AnomalyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux

@Service
class AnomalyService(@Autowired private val anomalyRepository: AnomalyRepository) : IAnomaly {

    @Value("\${anomaly.threshold:10}")
    private val defaultAnomalyThreshold: Int = 10


    override fun findAnomaliesByThermometerId(thermometerId: String): Flux<Anomaly> =
        anomalyRepository.findByThermometerId(thermometerId)

    override fun findAnomaliesByRoomId(roomId: String): Flux<Anomaly> =
        anomalyRepository.findByRoomId(roomId)


    override fun findThermometersWithAnomaliesAboveThreshold(threshold: Int?): Flux<Thermometer> =
        anomalyRepository.findAll()
            .groupBy(Anomaly::thermometerId)
            .flatMap { group ->
                group.count()
                    .filter { count -> count > (threshold ?: defaultAnomalyThreshold) }
                    .map { count -> Thermometer(group.key()!!, count) }
            }

}
