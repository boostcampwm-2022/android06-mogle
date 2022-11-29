package com.wakeup.data.source.local.xref

import com.wakeup.data.database.dao.XRefDao
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import javax.inject.Inject


class XRefLocalDataSourceImpl @Inject constructor(
    private val XRefDao: XRefDao,
) : XRefLocalDataSource {

    override suspend fun saveMomentPictureXRefs(momentPictures: List<MomentPictureXRef>) {
        XRefDao.saveMomentPictureXRefs(momentPictures)
    }

    override suspend fun saveMomentGlobeXRef(momentGlobe: MomentGlobeXRef) {
        XRefDao.saveMomentGlobeXRef(momentGlobe)
    }

    override suspend fun deleteMomentGlobeXRef(momentGlobe: MomentGlobeXRef) {
        XRefDao.deleteMomentGlobeXRef(momentGlobe)
    }
}
