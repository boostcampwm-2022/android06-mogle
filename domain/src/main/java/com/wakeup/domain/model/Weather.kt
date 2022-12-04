package com.wakeup.domain.model

data class Weather(
    val id: Long,
    val type: WeatherType,
    val iconUrl: String,
    val temperature: Double,
)
