package com.wakeup.data.source.local.moment

import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.GlobeEntity
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.entity.PictureEntity
import kotlinx.coroutines.flow.Flow

class MomentLocalDataSourceImpl(
    private val momentDao: MomentDao
): MomentLocalDataSource {

    override fun getMoments(): Flow<List<MomentEntity>> =
        momentDao.getMoments()

    override fun getPictures(momentId: Long): Flow<List<PictureEntity>> =
        momentDao.getPictures(momentId)

    override fun getGlobes(momentId: Long): Flow<List<GlobeEntity>> =
        momentDao.getGlobes(momentId)

    override fun saveMoment(moment: MomentEntity): Long =
        momentDao.saveMoment(moment)

    override fun savePicture(picture: List<PictureEntity>): List<Long> =
        momentDao.savePicture(picture)

    override fun saveMomentPicture(MomentPictures: List<MomentPictureEntity>) {
        momentDao.saveMomentPicture(MomentPictures)
    }

}