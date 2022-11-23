package com.wakeup.data.source.local.moment

import androidx.paging.PagingData
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.database.entity.MomentWithGlobesAndPictures
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.domain.model.SortType
import kotlinx.coroutines.flow.Flow

interface MomentLocalDataSource {

    fun getMoments(sortType: SortType, query: String, myLocation: LocationEntity?): Flow<PagingData<MomentWithGlobesAndPictures>>

/*    suspend fun getPictures(momentId: Long): List<PictureEntity>

    suspend fun getGlobes(momentId: Long): List<GlobeEntity>*/

    suspend fun getGlobeId(globeName: String): Long

    suspend fun saveMoment(moment: MomentEntity): Long

    suspend fun savePictures(pictures: List<PictureEntity>): List<Long>

    suspend fun saveMomentPictures(momentPictures :List<MomentPictureXRef>)

    suspend fun saveGlobes(globes: List<GlobeEntity>): List<Long>

    suspend fun saveMomentGlobe(momentGlobe :MomentGlobeXRef)
}