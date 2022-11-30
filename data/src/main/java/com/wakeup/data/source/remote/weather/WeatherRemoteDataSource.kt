package com.wakeup.data.source.remote.weather

import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.network.response.WeatherResponse

interface WeatherRemoteDataSource {

    suspend fun getWeatherData(
        location: LocationEntity
    ): Result<WeatherResponse>
}