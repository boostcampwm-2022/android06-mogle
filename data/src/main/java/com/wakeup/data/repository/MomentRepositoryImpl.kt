package com.wakeup.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.database.mapper.toEntity
import com.wakeup.data.source.local.globe.GlobeLocalDataSource
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.source.local.ref.RefLocalDataSource
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
    private val globeLocalDataSource: GlobeLocalDataSource,
    private val refLocalDataSource: RefLocalDataSource,
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

    override suspend fun saveMoment(moment: Moment) {
        val globeIndex = globeLocalDataSource.getGlobeId(moment.globes.first().name)

        if (moment.pictures.isEmpty()) {
            val momentIndex =
                momentLocalDataSource.saveMoment(moment.toEntity(moment.place, null))
            refLocalDataSource.saveMomentGlobeRef(
                MomentGlobeXRef(momentId = momentIndex, globeId = globeIndex)
            )
            return
        }

        val pictureFileNames = util.savePictureInInternalStorageAndGetFileName(moment.pictures)
        val pictureIndexes = momentLocalDataSource.savePictures(pictureFileNames)

        val momentIndex =
            momentLocalDataSource.saveMoment(moment.toEntity(moment.place, pictureIndexes.first()))

        refLocalDataSource.saveMomentPictureRefs(pictureIndexes.map { pictureId ->
            MomentPictureXRef(momentId = momentIndex, pictureId = pictureId)
        })
        refLocalDataSource.saveMomentGlobeRef(
            MomentGlobeXRef(momentId = momentIndex, globeId = globeIndex)
        )
    }
}