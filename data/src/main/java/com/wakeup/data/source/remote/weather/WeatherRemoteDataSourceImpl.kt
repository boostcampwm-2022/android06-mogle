package com.wakeup.data.source.remote.weather

import com.wakeup.data.network.request.LocationRequest
import com.wakeup.data.network.response.WeatherResponse
import com.wakeup.data.network.service.WeatherService
import javax.inject.Inject

/**
 *  날씨 코드 확인
 *  https://openweathermap.org/weather-conditions#Weather-Condition-Codes-2
 */

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val weatherService: WeatherService
) : WeatherRemoteDataSource {

    override suspend fun getWeatherData(location: LocationRequest): Result<WeatherResponse> {
        return Result.runCatching {
            weatherService.getWeatherData(
                location.latitude,
                location.longitude
            )
        }
    }
}