package com.wakeup.data.network.mapper

import com.wakeup.data.BuildConfig
import com.wakeup.data.network.request.LocationRequest
import com.wakeup.data.network.response.WeatherResponse
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Weather

fun WeatherResponse.toDomain(): Weather {
    return Weather(
        id = id,
        type = type,
        iconUrl = "${BuildConfig.WEATHER_ICON_BASE_URL}img/wn/$icon@4x.png",
        temperature = temperature
    )
}

fun Location.toRequest(): LocationRequest {
    return LocationRequest(
        latitude = latitude,
        longitude = longitude,
    )
}

fun LocationRequest.toDomain(): Location {
    return Location(
        latitude = latitude,
        longitude = longitude,
    )
}