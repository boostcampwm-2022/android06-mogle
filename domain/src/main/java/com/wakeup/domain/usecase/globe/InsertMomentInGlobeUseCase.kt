package com.wakeup.domain.usecase.globe

import com.wakeup.domain.model.Globe
import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.GlobeRepository
import com.wakeup.domain.repository.RelationRepository
import javax.inject.Inject

class InsertMomentInGlobeUseCase @Inject constructor(
    private val relationRepository: RelationRepository,
    private val globeRepository: GlobeRepository,
) {
    suspend operator fun invoke(moment: Moment, globe: Globe) {
        relationRepository.saveMomentGlobeXRef(moment.id, globe.id)

        if (globe.thumbnail == null && moment.pictures.isNotEmpty()) {
            globeRepository.updateGlobe(globe.copy(thumbnail = moment.pictures.first()))
        }
    }
}