package com.wakeup.domain.usecase.weather

import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Weather
import com.wakeup.domain.repository.WeatherRepository
import javax.inject.Inject

class GetWeatherDataUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {

    suspend operator fun invoke(location: Location): Result<Weather> {
        return weatherRepository.getWeatherData(location)
    }
}