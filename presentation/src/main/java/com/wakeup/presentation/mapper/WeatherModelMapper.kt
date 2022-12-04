package com.wakeup.presentation.mapper

import com.wakeup.domain.model.Weather
import com.wakeup.presentation.model.WeatherModel

fun Weather.toPresentation(): WeatherModel {
    return WeatherModel(
        id = id,
        type = type,
        iconUrl = iconUrl,
        temperature = temperature
    )
}