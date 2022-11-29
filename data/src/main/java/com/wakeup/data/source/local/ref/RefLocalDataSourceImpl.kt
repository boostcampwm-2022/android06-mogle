package com.wakeup.data.source.local.ref

import com.wakeup.data.database.dao.RefDao
import com.wakeup.data.database.entity.MomentGlobeXRef
import com.wakeup.data.database.entity.MomentPictureXRef
import javax.inject.Inject


class RefLocalDataSourceImpl @Inject constructor(
    private val refDao: RefDao,
) : RefLocalDataSource {

    override suspend fun saveMomentPictureRefs(momentPictures: List<MomentPictureXRef>) {
        refDao.saveMomentPictureRefs(momentPictures)
    }

    override suspend fun saveMomentGlobeRef(momentGlobe: MomentGlobeXRef) {
        refDao.saveMomentGlobeRef(momentGlobe)
    }

    override suspend fun deleteMomentGlobeRef(momentGlobe: MomentGlobeXRef) {
        refDao.deleteMomentGlobeRef(momentGlobe)
    }
}
