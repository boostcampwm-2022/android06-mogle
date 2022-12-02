package com.wakeup.data.source.remote.weather

import com.wakeup.data.network.request.LocationRequest
import com.wakeup.data.network.response.WeatherResponse

interface WeatherRemoteDataSource {

    suspend fun getWeatherData(
        location: LocationRequest
    ): Result<WeatherResponse>
}