package com.wakeup.data.di

import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.repository.moment.MomentRepositoryImpl
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.domain.repository.MomentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMomentRepository(
        momentDao: MomentDao,
        momentLocalDataSource: MomentLocalDataSource
    ): MomentRepository {
        return MomentRepositoryImpl(
            momentLocalDataSource
        )
    }
}