package com.wakeup.data.source.remote.weather

import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.network.response.WeatherResponse
import com.wakeup.data.network.service.WeatherService
import timber.log.Timber
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRemoteDataSource {

    override suspend fun getWeatherData(location: LocationEntity): Result<WeatherResponse> {
        return Result.runCatching {
            weatherService.getWeatherData(
                location.latitude,
                location.longitude
            )
        }
    }
}