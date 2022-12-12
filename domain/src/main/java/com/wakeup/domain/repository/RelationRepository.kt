package com.wakeup.domain.repository

interface RelationRepository {

    suspend fun saveMomentGlobeXRef(momentId: Long, globeId: Long)

    suspend fun deleteMomentGlobeXRef(momentId: Long, globeId: Long)
}