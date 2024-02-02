package io.kontak.apps.temperatureanalyticsapi.repository


import io.kontak.apps.event.Anomaly
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux

interface AnomalyRepository : ReactiveMongoRepository<Anomaly, String> {
    fun findByThermometerId(thermometerId: String): Flux<Anomaly>
    fun findByRoomId(roomId: String): Flux<Anomaly>
}
