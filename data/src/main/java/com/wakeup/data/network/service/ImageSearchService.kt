package com.wakeup.data.network.service

import com.wakeup.data.network.response.ImageSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ImageSearchService {

    @GET("/v2/search/image")
    suspend fun getImages(
        @Query("query") keyword: String,
        @Query("size") size: Int = 1,
    ): ImageSearchResponse
}