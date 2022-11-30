package com.wakeup.domain.usecase

import com.wakeup.domain.repository.GlobeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMomentCountByGlobeUseCase @Inject constructor(
    private val globeRepository: GlobeRepository
) {
    operator fun invoke(globeId: Long): Flow<Int> {
        return globeRepository.getMomentCountByGlobe(globeId)
    }
}