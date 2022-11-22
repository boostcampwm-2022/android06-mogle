package com.wakeup.data.source.remote.placeSearch

import com.wakeup.data.network.response.PlaceResponse

interface PlaceSearchRemoteDataSource {

    suspend fun search(keyword: String): Result<PlaceResponse>
}