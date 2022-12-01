package com.wakeup.domain.usecase

import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.GlobeRepository
import com.wakeup.domain.repository.MomentRepository
import com.wakeup.domain.repository.PictureRepository
import com.wakeup.domain.repository.RelationRepository
import javax.inject.Inject

class SaveMomentUseCase @Inject constructor(
    private val momentRepository: MomentRepository,
    private val pictureRepository: PictureRepository,
    private val globeRepository: GlobeRepository,
    private val relationRepository: RelationRepository,
) {
    suspend operator fun invoke(moment: Moment) {
        val globeId = globeRepository.getGlobeId(moment.globes.first().name)

        if (moment.pictures.isEmpty()) {
            val momentId = momentRepository.saveMoment(moment)
            relationRepository.saveMomentGlobeXRef(momentId, globeId)
        } else {
            val pictureIds = pictureRepository.savePictures(moment.pictures)
            val momentId = momentRepository.saveMoment(moment)
            relationRepository.saveMomentGlobeXRef(momentId, globeId)
            relationRepository.saveMomentPictureXRefs(momentId, pictureIds)
        }
    }
}