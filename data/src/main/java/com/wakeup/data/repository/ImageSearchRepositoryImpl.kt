package com.wakeup.data.repository

import com.wakeup.data.network.mapper.toDomain
import com.wakeup.data.source.remote.imageSearch.ImageSearchRemoteDataSource
import com.wakeup.domain.model.Picture
import com.wakeup.domain.repository.ImageSearchRepository
import javax.inject.Inject

class ImageSearchRepositoryImpl @Inject constructor(
    private val imageSearchRemoteDataSource: ImageSearchRemoteDataSource
) : ImageSearchRepository {
    override suspend fun search(keyword: String): Result<Picture> {
        return imageSearchRemoteDataSource.search(keyword).mapCatching {
            it.toDomain()
        }
    }
}