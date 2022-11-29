package com.wakeup.domain.usecase

import com.wakeup.domain.model.Globe
import com.wakeup.domain.repository.GlobeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGlobesUseCase @Inject constructor(
    private val globeRepository: GlobeRepository
) {
    operator fun invoke(): Flow<List<Globe>> {
        return globeRepository.getGlobes()
    }
}
