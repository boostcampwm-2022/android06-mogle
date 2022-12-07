package com.wakeup.domain.repository

import com.wakeup.domain.model.Picture

interface ImageSearchRepository {
    suspend fun search(keyword: String): Result<Picture>
}