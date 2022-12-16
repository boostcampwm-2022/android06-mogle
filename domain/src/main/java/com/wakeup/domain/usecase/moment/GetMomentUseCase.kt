package com.wakeup.domain.usecase.moment

import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMomentUseCase @Inject constructor(
    private val momentRepository: MomentRepository
) {

    operator fun invoke(momentId: Long): Flow<Moment> =
        momentRepository.getMoment(momentId)
}