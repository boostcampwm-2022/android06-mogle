package com.wakeup.data.repository

import com.wakeup.data.source.local.ref.RefLocalDataSource
import com.wakeup.domain.repository.RefRepository
import javax.inject.Inject

class RefRepositoryImpl @Inject constructor(
    private val refLocalDataSource: RefLocalDataSource,
) : RefRepository {

    override suspend fun saveMomentPictureRefs(momentId: Long, pictureIds: List<Long>) {
        TODO("Not yet implemented")
    }

    override suspend fun saveMomentGlobeRef(momentId: Long, globeId: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMomentGlobeRef(momentId: Long, globeId: Long) {
        TODO("Not yet implemented")
    }
}