package com.wakeup.domain.usecase.moment

import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import com.wakeup.domain.repository.PictureRepository
import com.wakeup.domain.repository.RelationRepository
import com.wakeup.domain.usecase.globe.InsertMomentInGlobeUseCase
import javax.inject.Inject

class SaveMomentUseCase @Inject constructor(
    private val momentRepository: MomentRepository,
    private val pictureRepository: PictureRepository,
    private val relationRepository: RelationRepository,
    private val insertMomentInGlobeUseCase: InsertMomentInGlobeUseCase,
) {
    suspend operator fun invoke(moment: Moment) {
        val momentId = momentRepository.saveMoment(moment)

        if (moment.pictures.isNotEmpty()) {
            val pictureIds = pictureRepository.savePictures(moment.pictures)
            relationRepository.saveMomentPictureXRefs(momentId, pictureIds)
        }

        val savedMoment = momentRepository.getMoment(momentId)
        insertMomentInGlobeUseCase(savedMoment, moment.globes.first())
    }
}