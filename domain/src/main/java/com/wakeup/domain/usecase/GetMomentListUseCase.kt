package com.wakeup.domain.usecase

import androidx.paging.PagingData
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.SortType
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMomentListUseCase @Inject constructor(
    private val momentRepository: MomentRepository
) {
    operator fun invoke(sort: SortType, query: String): Flow<PagingData<Moment>>{
        return momentRepository.getMoments(sort, query)
    }
}