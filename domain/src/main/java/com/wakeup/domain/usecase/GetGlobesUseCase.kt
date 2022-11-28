package com.wakeup.domain.usecase

import com.wakeup.domain.model.Globe
import com.wakeup.domain.repository.GlobeRepository
import javax.inject.Inject

class GetGlobesUseCase @Inject constructor(
    private val globeRepository: GlobeRepository
) {

    suspend operator fun invoke(): List<Globe> {
        return globeRepository.getGlobes()
    }
}
