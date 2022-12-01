package com.wakeup.data.source.local.picture

import com.wakeup.data.database.dao.PictureDao
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.data.source.local.moment.MomentLocalDataSourceImpl
import javax.inject.Inject

class PictureLocalDataSourceImpl @Inject constructor(
    private val pictureDao: PictureDao
) : PictureLocalDataSource {
    override suspend fun savePictures(pictures: List<PictureEntity>): List<Long> {
        val indexResult = pictureDao.savePictures(pictures).toMutableList()
        indexResult.forEachIndexed { pictureIndex, id ->
            if (id == MomentLocalDataSourceImpl.EXIST_INSERT_ERROR_CODE) {
                indexResult[pictureIndex] =
                    pictureDao.getPictureIdByByteArray(pictures[pictureIndex].path)
            }
        }
        return indexResult.toList()
    }
}