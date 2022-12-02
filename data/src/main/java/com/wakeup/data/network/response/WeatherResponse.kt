package com.wakeup.data.network.response

import com.wakeup.domain.model.WeatherType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * https://openweathermap.org/current
 */

@Serializable
data class WeatherResponse(
    val id: Long,
    val type: WeatherType,
    val icon: String,
    val temperature: Double,
)

@Serializable
data class WeatherTypeResponse(
    @SerialName("id") val id: Long,
    @SerialName("icon") val icon: String
)

@Serializable
data class TemperatureResponse(
    @SerialName("temp") val temperature: Double
)

@Serializable
data class WeatherContainerResponse(
    @SerialName("weather") val weathers: List<WeatherTypeResponse>,
    @SerialName("main") val main: TemperatureResponse
)