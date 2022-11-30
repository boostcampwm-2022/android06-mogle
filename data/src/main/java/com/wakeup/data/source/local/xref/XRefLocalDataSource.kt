package com.wakeup.data.source.local.xref

import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef

interface XRefLocalDataSource {

    suspend fun saveMomentPictureXRefs(momentPictures: List<MomentPictureXRef>)

    suspend fun saveMomentGlobeXRef(momentGlobe: MomentGlobeXRef)

    suspend fun deleteMomentGlobeXRef(momentGlobe: MomentGlobeXRef)
}