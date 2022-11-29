package com.wakeup.data.repository

import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.source.local.ref.RefLocalDataSource
import com.wakeup.domain.repository.RefRepository
import javax.inject.Inject

class RefRepositoryImpl @Inject constructor(
    private val refLocalDataSource: RefLocalDataSource,
) : RefRepository {

    override suspend fun saveMomentPictureRefs(momentId: Long, pictureIds: List<Long>) {
        refLocalDataSource.saveMomentPictureRefs(
            pictureIds.map { pictureId ->
                MomentPictureXRef(
                    momentId = momentId,
                    pictureId = pictureId
                )
            }
        )
    }

    override suspend fun saveMomentGlobeRef(momentId: Long, globeId: Long) {
        refLocalDataSource.saveMomentGlobeRef(
            MomentGlobeXRef(
                momentId = momentId,
                globeId = globeId
            )
        )
    }

    override suspend fun deleteMomentGlobeRef(momentId: Long, globeId: Long) {
        refLocalDataSource.deleteMomentGlobeRef(
            MomentGlobeXRef(
                momentId = momentId,
                globeId = globeId
            )
        )
    }
}