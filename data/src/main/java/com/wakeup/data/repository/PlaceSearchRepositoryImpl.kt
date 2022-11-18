package com.wakeup.data.repository

import com.wakeup.data.network.response.toDomain
import com.wakeup.data.source.remote.PlaceSearch.PlaceSearchRemoteDataSource
import com.wakeup.domain.model.Place
import com.wakeup.domain.repository.PlaceSearchRepository
import javax.inject.Inject

class PlaceSearchRepositoryImpl @Inject constructor(
    private val placeSearchRemoteDataSource: PlaceSearchRemoteDataSource
) : PlaceSearchRepository {

    override suspend fun search(keyword: String): List<Place> {
        return placeSearchRemoteDataSource.search(keyword).documents.map {
            it.toDomain()
        }
    }
}