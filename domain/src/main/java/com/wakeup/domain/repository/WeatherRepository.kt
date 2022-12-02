package com.wakeup.domain.repository

import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Weather

interface WeatherRepository {

    suspend fun getWeatherData(location: Location): Result<Weather>
}