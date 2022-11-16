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
/*
    saveMoment(
        Moment(
            "우리집",
            "쌍문동",
            listOf("image", "image2", "image3"),
            "우리집 강아지는 복스러운 강아지",
            listOf("default"),
            "2022-01-30"
        ),
        Location(
            "우리집",
            "쌍문동",
            37.0,
            37.0
        ),
        listOf("image", "image2", "image3"),
    )
    saveMoment(
        Moment(
            "우리집2",
            "쌍문동2",
            listOf("image", "image2", "image3"),
            "우리집 강아지는 복스러운 강아지",
            listOf("default"),
            "2022-01-30"
        ),
        Location(
            "우리집2",
            "쌍문동2",
            37.0,
            37.0
        ),
        listOf("image", "image2", "image3"),
    )
    saveMoment(
        Moment(
            "우리집3",
            "쌍문동3",
            listOf("image", "image2", "image3"),
            "우리집 강아지는 복스러운 강아지",
            listOf("default"),
            "2022-01-30"
        ),
        Location(
            "우리집3",
            "쌍문동3",
            37.0,
            37.0
        ),
        listOf("image", "image2", "image3"),
    )
}*/
