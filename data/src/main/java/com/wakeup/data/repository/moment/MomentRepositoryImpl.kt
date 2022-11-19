package com.wakeup.data.repository.moment

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.source.local.moment.MomentLocalPagingSource
import com.wakeup.data.toEntity
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import com.wakeup.domain.model.Picture
import com.wakeup.domain.repository.MomentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val ITEMS_PER_PAGE = 10

class MomentRepositoryImpl @Inject constructor(
    private val localDataSource: MomentLocalDataSource
) : MomentRepository {

    override fun getMoments(
        query: String,
        sort: String
    ): Flow<PagingData<Moment>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MomentLocalPagingSource(localDataSource, query, sort) }
        ).flow
    }

    override suspend fun saveMoment(moment: Moment, location: Location, pictures: List<Picture>) {
        val pictureIndexes = localDataSource.savePicture(pictures.map { it.toEntity() })
        val momentIndex = localDataSource.saveMoment(moment.toEntity(location, pictureIndexes[0]))

        localDataSource.saveMomentPicture(
            pictureIndexes.map { pictureId ->
                MomentPictureEntity(momentId = momentIndex, pictureId = pictureId)
            }
        )
    }
}
