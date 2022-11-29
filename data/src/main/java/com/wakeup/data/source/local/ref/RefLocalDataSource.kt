package com.wakeup.data.source.local.ref

import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef

interface RefLocalDataSource {

    suspend fun saveMomentPictureRefs(momentPictures: List<MomentPictureXRef>)

    suspend fun saveMomentGlobeRef(momentGlobe: MomentGlobeXRef)
}