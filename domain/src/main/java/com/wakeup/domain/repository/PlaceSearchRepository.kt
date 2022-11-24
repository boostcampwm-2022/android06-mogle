package com.wakeup.domain.repository

import com.wakeup.domain.model.Place


interface PlaceSearchRepository {

    suspend fun search(keyword: String): Result<List<Place>>
}