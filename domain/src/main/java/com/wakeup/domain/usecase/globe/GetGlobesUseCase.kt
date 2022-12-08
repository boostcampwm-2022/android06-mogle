package com.wakeup.domain.usecase.globe

import com.wakeup.domain.model.Globe
import com.wakeup.domain.repository.GlobeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetGlobesUseCase @Inject constructor(
    private val globeRepository: GlobeRepository,
) {
    operator fun invoke(): Flow<List<Globe>> {
        return globeRepository.getGlobes().map { globes ->
            globes.map { globe ->
                globe.copy(
                    momentCount = globeRepository.getMomentCountByGlobe(globe.id)
                )
            }
        }
    }
}
