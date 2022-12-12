package com.wakeup.domain.usecase.search

import com.wakeup.domain.model.Picture
import com.wakeup.domain.repository.ImageSearchRepository
import javax.inject.Inject

class SearchImageUseCase @Inject constructor(
    private val imageSearchRepository: ImageSearchRepository
) {
    suspend operator fun invoke(keyword: String): Result<Picture> {
        return imageSearchRepository.search(keyword)
    }
}