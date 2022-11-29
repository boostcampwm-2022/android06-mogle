package com.wakeup.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.database.mapper.toEntity
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.util.InternalFileUtil
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.SortType
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MomentRepositoryImpl @Inject constructor(
    private val momentLocalDataSource: MomentLocalDataSource,
    private val util: InternalFileUtil,
) : MomentRepository {

    override fun getMoments(
        sort: SortType,
        query: String,
        myLocation: Location?,
    ): Flow<PagingData<Moment>> =
        momentLocalDataSource.getMoments(sort, query, myLocation?.toEntity()).map { pagingData ->
            pagingData.map { momentInfo ->
                momentInfo.toDomain(
                    util.getPictureInInternalStorage(
                        momentInfo.pictures,
                        momentInfo.moment.thumbnailId
                    )
                )
            }
        }

    override suspend fun saveMoment(moment: Moment): Long {
        return momentLocalDataSource.saveMoment(moment.toEntity())
    }

    override suspend fun saveMomentWithPictures(moment: Moment): Pair<Long, List<Long>> {
        val pictureFileNames = util.savePictureInInternalStorageAndGetFileName(moment.pictures)
        val pictureIds = momentLocalDataSource.savePictures(pictureFileNames)

        val momentId = momentLocalDataSource.saveMoment(moment.toEntity(pictureIds.first()))
        return Pair(momentId, pictureIds)
    }
}