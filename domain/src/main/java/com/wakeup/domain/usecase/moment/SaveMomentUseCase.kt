package com.wakeup.domain.usecase.moment

import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import javax.inject.Inject

class SaveMomentUseCase @Inject constructor(
    private val momentRepository: MomentRepository
) {
    suspend operator fun invoke(moment: Moment) {
        momentRepository.saveMoment(moment)
    }
}