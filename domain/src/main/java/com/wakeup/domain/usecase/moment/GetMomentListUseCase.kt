package com.wakeup.domain.usecase.moment

import androidx.paging.PagingData
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.SortType
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMomentListUseCase @Inject constructor(
    private val momentRepository: MomentRepository
) {
    operator fun invoke(
        sortType: SortType,
        query: String,
        myLocation: Location?,
    ): Flow<PagingData<Moment>> {
        return momentRepository.getMoments(sortType, query, myLocation)
    }
}