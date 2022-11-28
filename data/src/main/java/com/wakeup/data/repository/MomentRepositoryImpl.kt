package com.wakeup.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
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
    private val localDataSource: MomentLocalDataSource,
    private val util: InternalFileUtil,
) : MomentRepository {

    override fun getMoments(
        sort: SortType,
        query: String,
        myLocation: Location?,
    ): Flow<PagingData<Moment>> =
        localDataSource.getMoments(sort, query, myLocation?.toEntity()).map { pagingData ->
            pagingData.map { momentInfo ->
                momentInfo.toDomain(
                    util.getPictureInInternalStorage(
                        momentInfo.pictureList,
                        momentInfo.moment.thumbnailId
                    ),
                    momentInfo.globeList
                )
            }
        }

    override suspend fun saveMoment(moment: Moment) {
        if (moment.pictures.isEmpty()) {
            localDataSource.saveMoment(moment.toEntity(moment.place, null))
            return
        }
        val pictureFileNames = util.savePictureInInternalStorageAndGetFileName(moment.pictures)
        val pictureIndexes = localDataSource.savePictures(pictureFileNames)

        // moment 추가할 때 항상 globe 하나 선택해서 추가(default 도 하나 선택해서 추가 임).
        val globeIndex =
            localDataSource.getGlobeId(moment.globes.first().name)
        val momentIndex =
            localDataSource.saveMoment(moment.toEntity(moment.place, pictureIndexes.first()))

        localDataSource.saveMomentPictures(
            pictureIndexes.map { pictureId ->
                MomentPictureXRef(momentId = momentIndex, pictureId = pictureId)
            }
        )
        localDataSource.saveMomentGlobe(
            MomentGlobeXRef(momentId = momentIndex, globeId = globeIndex)
        )
    }
}