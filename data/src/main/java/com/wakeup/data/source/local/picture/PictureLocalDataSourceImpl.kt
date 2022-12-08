package com.wakeup.data.source.local.picture

import com.wakeup.data.database.dao.PictureDao
import com.wakeup.data.database.entity.PictureEntity
import javax.inject.Inject

class PictureLocalDataSourceImpl @Inject constructor(
    private val pictureDao: PictureDao
) : PictureLocalDataSource {
    override suspend fun savePictures(pictures: List<PictureEntity>): List<Long> {
        val indexResult = pictureDao.savePictures(pictures).toMutableList()
        indexResult.forEachIndexed { pictureIndex, id ->
            if (id == EXIST_INSERT_ERROR_CODE) {
                indexResult[pictureIndex] =
                    pictureDao.getPictureIdByByteArray(pictures[pictureIndex].path)
            }
        }
        return indexResult.toList()
    }

    companion object {
        const val EXIST_INSERT_ERROR_CODE = -1L
    }
}