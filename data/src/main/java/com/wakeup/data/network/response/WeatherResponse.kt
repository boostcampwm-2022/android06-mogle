package com.wakeup.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * https://openweathermap.org/current
 */
@Serializable
data class WeatherResponse(
    @SerialName("id") val id: Long,
    @SerialName("main") val type: String,
    @SerialName("icon") val icon: String
)

@Serializable
data class WeathersResponse(
    val weathers: List<WeatherResponse>
)