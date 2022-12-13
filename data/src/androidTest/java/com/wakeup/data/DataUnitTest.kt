package com.wakeup.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.wakeup.data.database.MogleDatabase
import com.wakeup.data.database.dao.GlobeDao
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.dao.PictureDao
import com.wakeup.data.database.dao.XRefDao
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.data.database.entity.PlaceEntity
import com.wakeup.data.source.local.moment.MomentLocalDataSourceImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class DataUnitTest {
    private lateinit var testDatabase: MogleDatabase
    private lateinit var momentDao: MomentDao
    private lateinit var pictureDao: PictureDao
    private lateinit var xRefDao: XRefDao
    private lateinit var globeDao: GlobeDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        testDatabase = Room.inMemoryDatabaseBuilder(
            context, MogleDatabase::class.java
        ).build()
        momentDao = testDatabase.momentDao()
        pictureDao = testDatabase.pictureDao()
        xRefDao = testDatabase.xRefDao()
        globeDao = testDatabase.globeDao()
    }

    @After
    fun closeDb() {
        testDatabase.close()
    }

    @Test
    fun saveMomentAndGetMoments(): Unit = runBlocking {
        // 사진 리스트를 넣는다.
        // 1번 모먼트를 위한 picture 1, 2, 3
        // 2번 모먼트를 위한 picture 4, 1
        val saveReadyPictures = listOf(
            PictureEntity(path = "picture1"),
            PictureEntity(path = "picture2"),
            PictureEntity(path = "picture3"),
            PictureEntity(path = "picture4"),
            PictureEntity(path = "picture2")
        )
        val pictureIds = pictureDao.savePictures(saveReadyPictures)
        val correctPictures = getCorrectSavedPictures(pictureIds, saveReadyPictures)

        // 모먼트를 넣는다. 1번 모먼트, 2번 모먼트
        val momentId1 = momentDao.saveMoment(
            MomentEntity(
                place = PlaceEntity(
                    mainAddress = "우리집 1",
                    detailAddress = "쌍문동 1",
                    latitude = 37.5,
                    longitude = 36.5
                ),
                content = "안녕하세요 우리들",
                date = System.currentTimeMillis()
            )
        )

        val momentId2 = momentDao.saveMoment(
            MomentEntity(
                place = PlaceEntity(
                    mainAddress = "우리집 2",
                    detailAddress = "쌍문동 2",
                    latitude = 37.53,
                    longitude = 332.5
                ),
                content = "steadfastness",
                date = System.currentTimeMillis()
            )
        )

        // 관계를 넣는다
        // 1번 모먼트 - picture 1, 2, 3
        // 2번 모먼트 - picture 4, 1
        xRefDao.saveMomentPictureXRefs(
            listOf(
                MomentPictureXRef(momentId = momentId1, pictureId = correctPictures[0].id),
                MomentPictureXRef(momentId = momentId1, pictureId = correctPictures[1].id),
                MomentPictureXRef(momentId = momentId1, pictureId = correctPictures[2].id)
            )
        )

        xRefDao.saveMomentPictureXRefs(
            listOf(
                MomentPictureXRef(momentId = momentId2, pictureId = correctPictures[3].id),
                MomentPictureXRef(momentId = momentId2, pictureId = correctPictures[4].id),
            )
        )

        momentDao.getAllMoments(query = "우리집").first().map {
            println(it.moment)
            it.pictures.forEach { picture -> println(picture) }
            println()
        }
        println()
        xRefDao.getAllMomentPictureXRef().forEach {
            println(it)
        }
    }

    private suspend fun getCorrectSavedPictures(
        pictureIds: List<Long>,
        pictures: List<PictureEntity>,
    ): List<PictureEntity> {
        val tempPictures = mutableListOf<PictureEntity>()
        pictureIds.forEachIndexed { idx, id ->
            val path = pictures[idx].path
            if (id == MomentLocalDataSourceImpl.EXIST_INSERT_ERROR_CODE) {
                tempPictures.add(
                    PictureEntity(id = pictureDao.getPictureIdByByteArray(path), path = path)
                )
            } else {
                tempPictures.add(
                    PictureEntity(id = id, path = path)
                )
            }
        }
        return tempPictures.toList()
    }
}