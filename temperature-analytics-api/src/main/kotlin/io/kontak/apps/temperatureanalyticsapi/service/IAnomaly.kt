package io.kontak.apps.temperatureanalyticsapi.service

import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.Thermometer
import reactor.core.publisher.Flux

interface IAnomaly {

    fun findAnomaliesByThermometerId(thermometerId: String): Flux<Anomaly>

    fun findAnomaliesByRoomId(roomId: String): Flux<Anomaly>

    fun findThermometersWithAnomaliesAboveThreshold(threshold: Int? = 0): Flux<Thermometer>
}