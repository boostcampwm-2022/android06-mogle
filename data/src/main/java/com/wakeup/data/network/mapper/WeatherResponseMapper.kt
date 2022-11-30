package com.wakeup.data.network.mapper

import com.wakeup.data.BuildConfig
import com.wakeup.data.network.response.WeatherResponse
import com.wakeup.domain.model.Weather

fun WeatherResponse.toDomain(): Weather {
    return Weather(
        id = id,
        type = type,
        iconUrl = "${BuildConfig.WEATHER_BASE_URL}img/w/$icon.png"
    )
}