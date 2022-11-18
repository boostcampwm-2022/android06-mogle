package com.wakeup.data.source.remote.PlaceSearch

import com.wakeup.data.network.response.PlaceResponse

interface PlaceSearchRemoteDataSource {

    suspend fun search(keyword: String): PlaceResponse
}