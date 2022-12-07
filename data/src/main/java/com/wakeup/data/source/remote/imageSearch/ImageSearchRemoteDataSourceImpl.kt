package com.wakeup.data.source.remote.imageSearch

import com.wakeup.data.network.response.ImageSearchResponseItem
import com.wakeup.data.network.service.ImageSearchService
import javax.inject.Inject

class ImageSearchRemoteDataSourceImpl @Inject constructor(
    private val imageSearchService: ImageSearchService
) : ImageSearchRemoteDataSource {

    override suspend fun search(keyword: String): Result<ImageSearchResponseItem> {
        return Result.runCatching {
            imageSearchService.getImages(keyword).documents.first()
        }
    }
}