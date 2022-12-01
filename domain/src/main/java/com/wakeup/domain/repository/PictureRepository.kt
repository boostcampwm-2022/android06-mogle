package com.wakeup.domain.repository

import com.wakeup.domain.model.Picture

interface PictureRepository {

    suspend fun savePictures(pictures: List<Picture>): List<Long>

}