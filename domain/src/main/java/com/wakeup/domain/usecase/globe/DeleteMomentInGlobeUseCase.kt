package com.wakeup.domain.usecase.globe

import androidx.paging.PagingData
import com.wakeup.domain.model.Globe
import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.GlobeRepository
import com.wakeup.domain.repository.RelationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteMomentInGlobeUseCase @Inject constructor(
    private val relationRepository: RelationRepository,
    private val globeRepository: GlobeRepository,
) {
    suspend operator fun invoke(moment: Moment, globe: Globe) {
        relationRepository.deleteMomentGlobeXRef(moment.id, globe.id)

        if (globeRepository.getMomentsByGlobe(globe.id).first() == PagingData.empty<Moment>()) {
            globeRepository.updateGlobe(globe.copy(thumbnail = null))
        }
    }
}