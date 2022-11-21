package com.wakeup.data.repository.moment

import androidx.paging.PagingData
import androidx.paging.map
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.mapper.toDomain
import com.wakeup.data.database.mapper.toEntity
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.Picture
import com.wakeup.domain.model.Place
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class MomentRepositoryImpl @Inject constructor(
    private val localDataSource: MomentLocalDataSource,
) : MomentRepository {

    override fun getMoments(query: String, sort: String): Flow<PagingData<Moment>> =
        localDataSource.getMoments(query, sort).map { pagingData ->
            pagingData.map { momentEntity ->
                momentEntity.toDomain(
                    localDataSource.getPictures(momentEntity.id),
                    localDataSource.getGlobes(momentEntity.id)
                )
            }
        }

    override suspend fun saveMoment(moment: Moment, place: Place, pictures: List<Picture>) {
        Timber.d("asdsadsa: ${pictures.first().bitmap.size}")
        if (pictures.isEmpty()) {
            localDataSource.saveMoment(moment.toEntity(place, null))
            return
        } else {
            val pictureIndexes =
                localDataSource.savePicture(pictures.map { it.toEntity() })
            val momentIndex = 
                localDataSource.saveMoment(moment.toEntity(place, pictureIndexes[0]))

            localDataSource.saveMomentPicture(
                pictureIndexes.map { pictureId ->
                    MomentPictureEntity(momentId = momentIndex, pictureId = pictureId)
                }
            )
        }
    }
}
