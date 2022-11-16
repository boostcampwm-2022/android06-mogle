package com.wakeup.data.repository.moment

import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.toDomain
import com.wakeup.data.toEntity
import com.wakeup.data.toMomentPictureEntity
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map

class MomentRepositoryImpl(
    private val localDataSource: MomentLocalDataSource
) : MomentRepository {

    override fun getMoments(): Flow<List<Moment>> =
        localDataSource.getMoments().map { moments ->
            moments.map { momentEntity ->
                momentEntity.toDomain(
                    localDataSource.getPictures(momentEntity.id).last(),
                    localDataSource.getGlobes(momentEntity.id).last()
                )
            }
        }

    override fun saveMoment(moment: Moment, location: Location, pictures: List<String>) {
        val pictureIndexes = localDataSource.savePicture(pictures.map { it.toEntity() })
        val momentIndex = localDataSource.saveMoment(moment.toEntity(location, pictureIndexes[0]))

        localDataSource.saveMomentPicture(toMomentPictureEntity(momentIndex, pictureIndexes))
    }
}
