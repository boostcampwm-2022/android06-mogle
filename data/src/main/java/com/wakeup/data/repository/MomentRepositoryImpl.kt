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
                    )
                )
            }
        }

    override suspend fun saveMoment(moment: Moment) {
        val globeIndex = localDataSource.getGlobeId(moment.globes.first().name)

        if (moment.pictures.isEmpty()) {
            val momentIndex =
                localDataSource.saveMoment(moment.toEntity(moment.place, null))
            localDataSource.saveMomentGlobe(
                MomentGlobeXRef(momentId = momentIndex, globeId = globeIndex)
            )
            return
        }

        val pictureFileNames = util.savePictureInInternalStorageAndGetFileName(moment.pictures)
        val pictureIndexes = localDataSource.savePictures(pictureFileNames)

        val momentIndex =
            localDataSource.saveMoment(moment.toEntity(moment.place, pictureIndexes.first()))

        localDataSource.saveMomentPictures(pictureIndexes.map { pictureId ->
            MomentPictureXRef(momentId = momentIndex, pictureId = pictureId)
        })
        localDataSource.saveMomentGlobe(
            MomentGlobeXRef(momentId = momentIndex, globeId = globeIndex)
        )
    }
}