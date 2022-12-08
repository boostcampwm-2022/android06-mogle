package com.wakeup.data.repository

import com.wakeup.data.network.mapper.toDomain
import com.wakeup.data.source.remote.placeSearch.PlaceSearchRemoteDataSource
import com.wakeup.domain.model.Place
import com.wakeup.domain.repository.PlaceSearchRepository
import javax.inject.Inject

class PlaceSearchRepositoryImpl @Inject constructor(
    private val placeSearchRemoteDataSource: PlaceSearchRemoteDataSource
) : PlaceSearchRepository {

    override suspend fun search(keyword: String): Result<List<Place>> {
        return placeSearchRemoteDataSource.search(keyword).mapCatching {
            it.documents.map { item -> item.toDomain() }
        }
    }
}