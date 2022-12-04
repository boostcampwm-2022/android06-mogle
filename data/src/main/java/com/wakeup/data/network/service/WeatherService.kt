package com.wakeup.data.network.service

import com.wakeup.data.network.response.WeatherResponse
import com.wakeup.data.network.response.WeatherTypeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") tempType: String = "metric",
    ): WeatherResponse
}