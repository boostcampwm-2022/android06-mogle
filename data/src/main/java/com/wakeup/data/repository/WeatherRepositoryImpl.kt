package com.wakeup.data.repository

import com.wakeup.data.database.mapper.toEntity
import com.wakeup.data.network.mapper.toDomain
import com.wakeup.data.network.mapper.toRequest
import com.wakeup.data.source.remote.weather.WeatherRemoteDataSource
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Weather
import com.wakeup.domain.repository.WeatherRepository
import timber.log.Timber
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {

    override suspend fun getWeatherData(location: Location): Result<Weather> {
        return weatherRemoteDataSource.getWeatherData(
            location.toRequest()
        ).mapCatching {
            it.toDomain()
        }
    }
}