package com.wakeup.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.wakeup.data.database.MogleDatabase
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentPictureEntity
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.data.model.LocationEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DataUnitTest {
    private lateinit var testDatabase: MogleDatabase
    private lateinit var momentDao: MomentDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        testDatabase = Room.inMemoryDatabaseBuilder(
            context, MogleDatabase::class.java
        ).build()
        momentDao = testDatabase.momentDao()
    }

    @After
    fun closeDb() {
        testDatabase.close()
    }

    @Test
    fun saveMomentAndGetMoments(): Unit = runBlocking {
        val pictureId = momentDao.savePicture(listOf(
            PictureEntity(
                bitmap = "picture1".toByteArray()
            ),
            PictureEntity(
                bitmap = "picture2".toByteArray()
            ),
            PictureEntity(
                bitmap = "picture3".toByteArray()
            )
        ))
        val momentId1 = momentDao.saveMoment(MomentEntity(
            location = LocationEntity(
                mainAddress = "우리집",
                detailAddress = "쌍문동",
                latitude = 37.5,
                longitude = 36.5
            ),
            thumbnailId = pictureId[0],
            content = "안녕하세요 우리들",
            date = "2022-07-18"
        ))

        val momentId2 = momentDao.saveMoment(MomentEntity(
            location = LocationEntity(
                mainAddress = "우리집zzz",
                detailAddress = "쌍문동zzz",
                latitude = 37.53,
                longitude = 332.5
            ),
            thumbnailId = pictureId[1],
            content = "steadfastness",
            date = "2022-07-22"
        ))

        momentDao.saveMomentPicture(listOf(
            MomentPictureEntity(
                momentId = momentId1,
                pictureId = pictureId[0]
            ),
            MomentPictureEntity(
                momentId = momentId1,
                pictureId = pictureId[1]
            ),
            MomentPictureEntity(
                momentId = momentId1,
                pictureId = pictureId[2]
            )
        ))

        momentDao.saveMomentPicture(listOf(
            MomentPictureEntity(
                momentId = momentId2,
                pictureId = pictureId[0]
            ),
            MomentPictureEntity(
                momentId = momentId2,
                pictureId = pictureId[1]
            ),
            MomentPictureEntity(
                momentId = momentId2,
                pictureId = pictureId[2]
            )
        ))

        println(momentDao.getMoments().first())
    }
}