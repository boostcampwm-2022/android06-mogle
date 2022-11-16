package com.wakeup.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.wakeup.data.database.MogleDatabase
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.repository.moment.MomentRepositoryImpl
import com.wakeup.data.source.local.moment.MomentLocalDataSourceImpl
import com.wakeup.domain.model.Location
import com.wakeup.domain.model.Moment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DataUnitTest {
    private lateinit var testDatabase: MogleDatabase
    private lateinit var momentDao: MomentDao
    private lateinit var momentLocalDataSourceImpl: MomentLocalDataSourceImpl
    private lateinit var momentRepositoryImpl: MomentRepositoryImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        testDatabase = Room.inMemoryDatabaseBuilder(
            context, MogleDatabase::class.java
        ).build()
        momentDao = testDatabase.momentDao()

        momentLocalDataSourceImpl = MomentLocalDataSourceImpl(momentDao)
        momentRepositoryImpl = MomentRepositoryImpl(momentLocalDataSourceImpl)
    }

    @After
    fun closeDb() {
        testDatabase.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveMomentAndGetMoments() = runTest(UnconfinedTestDispatcher()) {
        momentRepositoryImpl.saveMoment(
            Moment(
                "우리집", "쌍문동", listOf("image", "image2", "image3"),
                "우리집 강아지는 복스러운 강아지", listOf("default"), "2022-01-30"
            ),
            Location(
                "우리집", "쌍문동", 37.0, 37.0
            ),
            listOf("image", "image2", "image3"),
        )
        momentRepositoryImpl.saveMoment(
            Moment(
                "우리집2", "쌍문동2", listOf("image2", "image22", "image23"),
                "우리집 강아지는 복스러운 강아지2", listOf("default"), "2022-01-30"
            ),
            Location(
                "우리집", "쌍문동", 37.0, 37.0
            ),
            listOf("image", "image22", "image33"),
        )
        momentRepositoryImpl.saveMoment(
            Moment(
                "우리집", "쌍문동3", listOf("image", "image23", "image3"),
                "우리집 강아지는 복스러운 강아지3", listOf("default"), "2022-01-30"
            ),
            Location(
                "우리집", "쌍문동3", 37.0, 37.0
            ),
            listOf("image", "image23", "image3"),
        )
        println(momentRepositoryImpl.getMoments().last())
    }
}