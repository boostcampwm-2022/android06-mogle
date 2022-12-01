package com.wakeup.data.network.mapper

import com.wakeup.data.BuildConfig
import com.wakeup.data.network.request.LocationRequest
import com.wakeup.data.network.response.WeatherResponse
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Weather
import kotlin.math.round

private fun Double.toKelvin() = round((this - 273.15) * 100) / 100

fun WeatherResponse.toDomain(): Weather {
    return Weather(
        id = id,
        type = type,
        iconUrl = "${BuildConfig.WEATHER_BASE_URL}img/w/$icon.png",
        temperature = temperature.toKelvin()
    )
}

fun LocationRequest.toDomain(): Location {
    return Location(
        latitude = latitude,
        longitude = longitude,
    )
}