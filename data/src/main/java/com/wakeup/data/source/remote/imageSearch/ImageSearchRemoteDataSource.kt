package com.wakeup.data.source.remote.imageSearch

import com.wakeup.data.network.response.ImageSearchResponseItem

interface ImageSearchRemoteDataSource {

    suspend fun search(keyword: String): Result<ImageSearchResponseItem>
}