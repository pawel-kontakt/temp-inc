package io.kontak.apps.temperatureanalyticsapi.controller

import io.kontak.apps.event.Anomaly
import io.kontak.apps.event.Thermometer
import io.kontak.apps.temperatureanalyticsapi.service.AnomalyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class AnomalyController(@Autowired private val anomalyService: AnomalyService) {

    @GetMapping("/anomalies/thermometer/{thermometerId}")
    fun getAnomaliesByThermometerId(@PathVariable thermometerId: String): Flux<Anomaly> =
        anomalyService.findAnomaliesByThermometerId(thermometerId)

    @GetMapping("/anomalies/room/{roomId}")
    fun getAnomaliesByRoomId(@PathVariable roomId: String): Flux<Anomaly> =
        anomalyService.findAnomaliesByRoomId(roomId)

    @GetMapping("/thermometers/anomalies")
    fun getThermometersWithAnomaliesAboveThreshold(@RequestParam(required = false) threshold: Int?): Flux<Thermometer> =
        anomalyService.findThermometersWithAnomaliesAboveThreshold(threshold)
}
