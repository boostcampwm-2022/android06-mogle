package com.wakeup.data.source.remote.PlaceSearch

import com.wakeup.data.network.response.PlaceResponse
import com.wakeup.data.network.service.PlaceSearchService
import javax.inject.Inject

class PlaceSearchRemoteDataSourceImpl @Inject constructor(
    private val placeSearchService: PlaceSearchService
) : PlaceSearchRemoteDataSource {

    override suspend fun search(keyword: String): PlaceResponse {
        return placeSearchService.getPlaces(keyword)
    }
}