package com.wakeup.domain.usecase

import com.wakeup.domain.model.Place
import com.wakeup.domain.repository.PlaceSearchRepository
import javax.inject.Inject

class SearchPlaceUseCase @Inject constructor(
    private val placeSearchRepository: PlaceSearchRepository
) {

    suspend operator fun invoke(keyword: String): List<Place> {
        return placeSearchRepository.search(keyword)
    }
}