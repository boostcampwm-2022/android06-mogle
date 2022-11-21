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
import javax.inject.Inject

class MomentRepositoryImpl @Inject constructor(
    private val localDataSource: MomentLocalDataSource,
) : MomentRepository {

    override fun getMoments(
        query: String,
        sort: String,
    ): Flow<PagingData<Moment>> =
        localDataSource.getMoments(query, sort).map { pagingData ->
            pagingData.map { momentEntity ->
                momentEntity.toDomain(
                    localDataSource.getPictures(momentEntity.id),
                    localDataSource.getGlobes(momentEntity.id)
                )
            }
        }

    override suspend fun saveMoment(moment: Moment, location: Place, pictures: List<Picture>?) {
        if (pictures == null) {
            localDataSource.saveMoment(moment.toEntity(location, null))
        } else {
            val pictureIndexes = localDataSource.savePicture(pictures.map { it.toEntity() })
            val momentIndex = localDataSource.saveMoment(moment.toEntity(location, pictureIndexes[0]))

            localDataSource.saveMomentPicture(
                pictureIndexes.map { pictureId ->
                    MomentPictureEntity(momentId = momentIndex, pictureId = pictureId)
                }
            )
        }
    }
}
