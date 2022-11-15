package com.wakeup.data.source.local.moment

import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.source.toDomain
import com.wakeup.domain.model.Moment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MomentLocalDataSourceImpl(
    private val momentDao: MomentDao
): MomentLocalDataSource {

    override fun getMoments(): Flow<List<Moment>> =
        momentDao.getMoments().map { moments ->
            moments.map { moment ->
                moment.toDomain(
                    momentDao.getLocation(moment.locationId),
                    momentDao.getPictures(moment.id),
                    momentDao.getGlobes(moment.id)
                )
            }
        }
}