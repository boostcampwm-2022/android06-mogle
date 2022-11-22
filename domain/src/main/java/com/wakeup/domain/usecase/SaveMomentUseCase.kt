package com.wakeup.domain.usecase

import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import javax.inject.Inject

class SaveMomentUseCase @Inject constructor(
    private val localRepository: MomentRepository
) {
    suspend operator fun invoke(moment: Moment) {
        localRepository.saveMoment(moment)
    }
}