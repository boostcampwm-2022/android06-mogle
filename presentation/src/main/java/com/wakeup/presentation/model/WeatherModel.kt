package com.wakeup.presentation.model

import com.wakeup.domain.model.WeatherType

data class WeatherModel(
    val id: Long,
    val type: WeatherType,
    val iconUrl: String,
    val temperature: Double,
)