package com.wakeup.data.source.local.picture

import com.wakeup.data.database.entity.PictureEntity

interface PictureLocalDataSource {
    suspend fun savePictures(pictures: List<PictureEntity>): List<Long>
}