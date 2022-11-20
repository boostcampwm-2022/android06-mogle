package com.wakeup.domain.usecase

import androidx.paging.PagingData
import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMomentListUseCase @Inject constructor(
    private val momentRepository: MomentRepository
) {
    operator fun invoke(query: String, sort: String): Flow<PagingData<Moment>>{
        return momentRepository.getMoments(query, sort)
    }
}