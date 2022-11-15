package com.wakeup.data.source.local.moment

import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.LocationEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.PictureEntity
import kotlinx.coroutines.flow.Flow

class MomentLocalDataSourceImpl(
    private val momentDao: MomentDao
): MomentLocalDataSource {

    override fun getMoments(): Flow<List<MomentEntity>> =
        momentDao.getMoments()

    override fun getLocation(momentId: Int): Flow<LocationEntity> =
        momentDao.getLocation(momentId)

    override fun getPictures(momentId: Int): Flow<List<PictureEntity>> =
        momentDao.getPictures(momentId)

    override fun getGlobes(momentId: Int): Flow<List<GlobeEntity>> =
        momentDao.getGlobes(momentId)


}