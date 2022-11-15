package com.wakeup.domain.usecase

import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow

class GetMomentListUseCase(
    private val localRepository: MomentRepository
) {
    operator fun invoke() : Flow<List<Moment>> = localRepository.getMoments()
}