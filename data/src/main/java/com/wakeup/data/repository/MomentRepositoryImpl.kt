package com.wakeup.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.database.mapper.toEntity
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.SortType
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MomentRepositoryImpl @Inject constructor(
    private val momentLocalDataSource: MomentLocalDataSource,
) : MomentRepository {

    override fun getMoments(
        sort: SortType,
        query: String,
        myLocation: Location?,
    ): Flow<PagingData<Moment>> =
        momentLocalDataSource.getMoments(sort, query, myLocation?.toEntity()).map { pagingData ->
            pagingData.map { momentInfo ->
                momentInfo.toDomain()
            }
        }

    override fun getAllMoments(query: String): Flow<List<Moment>> =
        momentLocalDataSource.getAllMoments(query).map { momentInfoList ->
            momentInfoList.map { momentInfo ->
                momentInfo.toDomain()
            }
        }

    override suspend fun getMoment(id: Long): Moment {
        return momentLocalDataSource.getMoment(id).toDomain()
    }

    override suspend fun saveMoment(moment: Moment): Long {
        return momentLocalDataSource.saveMoment(moment.toEntity())
    }
}