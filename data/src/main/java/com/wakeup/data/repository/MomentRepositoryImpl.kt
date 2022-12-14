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
import timber.log.Timber
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
            pagingData.map { momentXRefs ->
                momentXRefs.toDomain()
            }
        }

    override fun getAllMoments(query: String): Flow<List<Moment>> =
        momentLocalDataSource.getAllMoments(query).map { momentInfoList ->
            momentInfoList.map { momentXRefs -> momentXRefs.toDomain() }
        }

    override fun getMoment(momentId: Long): Flow<Moment> =
        momentLocalDataSource.getMoment(momentId).map { momentXRefs -> momentXRefs.toDomain() }

    override suspend fun saveMoment(moment: Moment) {
        return momentLocalDataSource.saveMoment(moment.toEntity())
    }

    override suspend fun deleteMoment(momentId: Long) {
        momentLocalDataSource.deleteMoment(momentId)
    }

    override suspend fun updateMoment(moment: Moment) {
        momentLocalDataSource.updateMoment(moment.toEntity())
    }
}