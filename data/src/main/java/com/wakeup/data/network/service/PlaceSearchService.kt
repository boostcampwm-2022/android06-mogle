package com.wakeup.data.network.service

import com.wakeup.data.network.response.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceSearchService {

    @GET("/v2/local/search/keyword.json")
    suspend fun getPlaces(
        @Query("query") keyword: String,
        @Query("start") start: Int = 1,
        @Query("display") display: Int = 15,
    ): PlaceResponse
}