package com.wakeup.domain.repository

interface RelationRepository {

    suspend fun saveMomentPictureXRefs(momentId: Long, pictureIds: List<Long>)

    suspend fun saveMomentGlobeXRef(momentId: Long, globeId: Long)

    suspend fun deleteMomentGlobeXRef(momentId: Long, globeId: Long)
}