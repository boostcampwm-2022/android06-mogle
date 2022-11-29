package com.wakeup.domain.repository

interface RefRepository {

    suspend fun saveMomentPictureRefs(momentId: Long, pictureIds: List<Long>)

    suspend fun saveMomentGlobeRef(momentId: Long, globeId: Long)

    suspend fun deleteMomentGlobeRef(momentId: Long, globeId: Long)
}