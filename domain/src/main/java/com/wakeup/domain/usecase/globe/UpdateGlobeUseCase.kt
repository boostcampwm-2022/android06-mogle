package com.wakeup.domain.usecase.globe

import com.wakeup.domain.model.Globe
import com.wakeup.domain.repository.GlobeRepository
import javax.inject.Inject

class UpdateGlobeUseCase @Inject constructor(
    private val globeRepository: GlobeRepository
) {
    suspend operator fun invoke(globe: Globe) {
        globeRepository.updateGlobe(globe)
    }
}
