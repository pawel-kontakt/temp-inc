package io.konkat.apps.storage

import io.kontak.apps.event.Anomaly
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

@Service
class AnomalyService(private val reactiveMongoTemplate: ReactiveMongoTemplate) {

    @KafkaListener(topics = ["temperature-anomalies"])
    fun consumeAnomaly(anomaly: Anomaly) {
        reactiveMongoTemplate.save(anomaly).subscribe()
    }

}