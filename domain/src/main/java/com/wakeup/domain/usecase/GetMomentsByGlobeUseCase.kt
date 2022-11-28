package com.wakeup.domain.usecase

import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.GlobeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMomentsByGlobeUseCase @Inject constructor(
    private val globeRepository: GlobeRepository
) {
    operator fun invoke(globeId: Long): Flow<List<Moment>> {
        return globeRepository.getMomentsByGlobe(globeId)
    }
}