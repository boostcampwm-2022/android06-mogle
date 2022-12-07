package com.wakeup.domain.usecase.moment

import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import com.wakeup.domain.repository.PictureRepository
import com.wakeup.domain.repository.RelationRepository
import javax.inject.Inject

class SaveMomentUseCase @Inject constructor(
    private val momentRepository: MomentRepository,
    private val pictureRepository: PictureRepository,
    private val relationRepository: RelationRepository,
) {
    suspend operator fun invoke(moment: Moment) {
        val globeId = moment.globes.first().id

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