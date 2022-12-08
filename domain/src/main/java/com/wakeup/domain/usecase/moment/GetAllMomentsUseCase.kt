package com.wakeup.domain.usecase.moment

import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMomentsUseCase @Inject constructor(
    private val momentRepository: MomentRepository
) {

    operator fun invoke(query: String): Flow<List<Moment>> {
        return momentRepository.getAllMoments(query)
    }
}