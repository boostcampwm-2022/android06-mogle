package com.wakeup.data.repository

import com.wakeup.data.database.mapper.toEntity
import com.wakeup.data.source.local.picture.PictureLocalDataSource
import com.wakeup.data.util.InternalFileUtil
import com.wakeup.domain.model.Picture
import com.wakeup.domain.repository.PictureRepository
import javax.inject.Inject

class PictureRepositoryImpl @Inject constructor(
    private val pictureLocalDataSource: PictureLocalDataSource,
    private val util: InternalFileUtil,
) : PictureRepository {
    override suspend fun savePictures(pictures: List<Picture>): List<Long> {
        pictures.forEach { picture ->
            util.savePictureInInternalStorage(picture)
        }
        return pictureLocalDataSource.savePictures(pictures.map { picture -> picture.toEntity() })
    }

}