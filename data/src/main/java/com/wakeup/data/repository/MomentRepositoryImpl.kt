package com.wakeup.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.wakeup.data.database.entity.GlobeEntity
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MomentRepositoryImpl @Inject constructor(
    private val localDataSource: MomentLocalDataSource,
    private val util: InternalFileUtil,
) : MomentRepository {

    init {
        // todo: globe_test_code (must remove to release)
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.saveGlobes(listOf(
                GlobeEntity(name = "default"),
                GlobeEntity(name = "globe 1"),
                GlobeEntity(name = "globe 2"),
                GlobeEntity(name = "globe 3"))
            )
        }
    }

    override fun getMoments(
        sort: SortType,
        query: String,
        myLocation: Location?,
    ): Flow<PagingData<Moment>> =
        localDataSource.getMoments(sort, query, myLocation?.toEntity()).map { pagingData ->
            pagingData.map { momentInfo ->
                momentInfo.toDomain(util.getPictureInInternalStorage(momentInfo.pictureList),
                    momentInfo.globeList)
            }
        }

    override suspend fun saveMoment(moment: Moment) {
        if (moment.pictures.isEmpty()) {
            localDataSource.saveMoment(moment.toEntity(moment.place, null))
            return
        }
        val pictureFileNames = util.savePictureInInternalStorageAndGetFileName(moment.pictures)
        val pictureIndexes = localDataSource.savePictures(pictureFileNames)

        // 정책: moment 추가할 때 항상 globe 하나 선택해서 추가(default 도 하나 선택해서 추가 임).
        val globeIndex = localDataSource.getGlobeId(moment.globes[0].name)
        val momentIndex =
            localDataSource.saveMoment(moment.toEntity(moment.place, pictureIndexes[0]))

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
