package com.wakeup.domain.usecase

import com.wakeup.domain.repository.MomentRepository
import javax.inject.Inject

class DeleteMomentUseCase @Inject constructor(
    private val momentRepository: MomentRepository
) {

    suspend operator fun invoke(momentId: Long) {
        momentRepository.deleteMoment(momentId)
    }
}