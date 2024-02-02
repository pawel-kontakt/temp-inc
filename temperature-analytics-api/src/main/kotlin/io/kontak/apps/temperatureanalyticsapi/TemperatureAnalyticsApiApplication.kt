package io.kontak.apps.temperatureanalyticsapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TemperatureAnalyticsApiApplication

fun main(args: Array<String>) {
	runApplication<TemperatureAnalyticsApiApplication>(*args)
}
