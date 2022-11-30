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
                        momentInfo.pictures,
                        momentInfo.moment.thumbnailId
                    )
                )
            }
        }

    override fun getAllMoments(): Flow<List<Moment>> =
        localDataSource.getAllMoments().map { momentInfoList ->
            momentInfoList.map { momentInfo ->
                momentInfo.toDomain(util.getPictureInInternalStorage(momentInfo.pictures,
                    momentInfo.moment.thumbnailId))
            }
        }

    override suspend fun saveMoment(moment: Moment) {
        val globeIndex = localDataSource.getGlobeId(moment.globes.first().name)

        // 사진 없다면, 즉시 모먼트 저장
        if (moment.pictures.isEmpty()) {
            val momentIndex =
                localDataSource.saveMoment(moment.toEntity(moment.place, null))
            localDataSource.saveMomentGlobe(
                MomentGlobeXRef(momentId = momentIndex, globeId = globeIndex)
            )
            return
        }

        // 1. 내부 저장소에 이미지 저장
        val pictureFileNames = util.savePictureInInternalStorageAndGetFileName(moment.pictures)
        val pictureIndexes = localDataSource.savePictures(pictureFileNames)

        // 2. 모먼트 DB 저장
        val momentIndex =
            localDataSource.saveMoment(moment.toEntity(moment.place, pictureIndexes.first()))

        // 3. [모먼트 - 사진] DB 저장
        localDataSource.saveMomentPictures(pictureIndexes.map { pictureId ->
            MomentPictureXRef(momentId = momentIndex, pictureId = pictureId)
        })

        // 4. [모먼트 - 글로브] DB 저장
        localDataSource.saveMomentGlobe(
            MomentGlobeXRef(momentId = momentIndex, globeId = globeIndex)
        )
    }
}