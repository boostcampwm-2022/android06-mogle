package com.wakeup.data.repository

import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.source.local.xref.XRefLocalDataSource
import com.wakeup.domain.repository.RelationRepository
import javax.inject.Inject

class RelationRepositoryImpl @Inject constructor(
    private val XRefLocalDataSource: XRefLocalDataSource,
) : RelationRepository {

    override suspend fun saveMomentGlobeXRef(momentId: Long, globeId: Long) {
        XRefLocalDataSource.saveMomentGlobeXRef(
            MomentGlobeXRef(
                momentId = momentId,
                globeId = globeId
            )
        )
    }

    override suspend fun deleteMomentGlobeXRef(momentId: Long, globeId: Long) {
        XRefLocalDataSource.deleteMomentGlobeXRef(
            MomentGlobeXRef(
                momentId = momentId,
                globeId = globeId
            )
        )
    }
}