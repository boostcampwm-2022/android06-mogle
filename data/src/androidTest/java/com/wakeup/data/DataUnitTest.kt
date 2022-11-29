package com.wakeup.data

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.wakeup.data.database.MogleDatabase
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.database.dao.RefDao
import com.wakeup.data.database.entity.MomentEntity
import com.wakeup.data.database.entity.MomentPictureXRef
import com.wakeup.data.database.entity.PictureEntity
import com.wakeup.data.database.entity.PlaceEntity
import com.wakeup.data.source.local.moment.MomentLocalDataSourceImpl
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.firstOrNull
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
    private lateinit var refDao: RefDao


    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        testDatabase = Room.inMemoryDatabaseBuilder(
            context, MogleDatabase::class.java
        ).build()
        momentDao = testDatabase.momentDao()
        refDao = testDatabase.refDao()
    }

    @After
    fun closeDb() {
        testDatabase.close()
    }

    @Test
    fun saveMomentAndGetMoments(): Unit = runBlocking {
        val pictureId = momentDao.savePictures(
            listOf(
                PictureEntity(
                    fileName = "picture1"
                ),
                PictureEntity(
                    fileName = "picture2"
                ),
                PictureEntity(
                    fileName = "picture3"
                )
            )
        )
        val momentId1 = momentDao.saveMoment(
            MomentEntity(
                place = PlaceEntity(
                    mainAddress = "우리집",
                    detailAddress = "쌍문동",
                    latitude = 37.5,
                    longitude = 36.5
                ),
                thumbnailId = pictureId[0],
                content = "안녕하세요 우리들",
                date = System.currentTimeMillis()
            )
        )

        val momentId2 = momentDao.saveMoment(
            MomentEntity(
                place = PlaceEntity(
                    mainAddress = "우리집zzz",
                    detailAddress = "쌍문동zzz",
                    latitude = 37.53,
                    longitude = 332.5
                ),
                thumbnailId = pictureId[1],
                content = "steadfastness",
                date = System.currentTimeMillis()
            )
        )

        refDao.saveMomentPictureRefs(
            listOf(
                MomentPictureXRef(
                    momentId = momentId1,
                    pictureId = pictureId[0]
                ),
                MomentPictureXRef(
                    momentId = momentId1,
                    pictureId = pictureId[1]
                ),
                MomentPictureXRef(
                    momentId = momentId1,
                    pictureId = pictureId[2]
                )
            )
        )

        refDao.saveMomentPictureRefs(
            listOf(
                MomentPictureXRef(
                    momentId = momentId2,
                    pictureId = pictureId[0]
                ),
                MomentPictureXRef(
                    momentId = momentId2,
                    pictureId = pictureId[1]
                ),
                MomentPictureXRef(
                    momentId = momentId2,
                    pictureId = pictureId[2]
                )
            )
        )

        val result = Pager(
            config = PagingConfig(
                pageSize = MomentLocalDataSourceImpl.ITEMS_PER_PAGE,
                enablePlaceholders = false,
                initialLoadSize = 10,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { momentDao.getMoments(0, "") }
        ).flow.firstOrNull()

        assertEquals(result.toString(), "androidx.paging.PagingData@28c0368")
    }
}