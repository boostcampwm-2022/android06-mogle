package com.wakeup.data.source.local.xref

import com.wakeup.data.database.entity.MomentGlobeXRef

interface XRefLocalDataSource {

    suspend fun saveMomentGlobeXRef(momentGlobe: MomentGlobeXRef)

    suspend fun deleteMomentGlobeXRef(momentGlobe: MomentGlobeXRef)
}