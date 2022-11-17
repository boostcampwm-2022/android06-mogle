package com.wakeup.data.di

import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.source.local.moment.MomentLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    fun provideMomentLocalDataSource(
        momentDao: MomentDao
    ): MomentLocalDataSource = MomentLocalDataSourceImpl(momentDao)
}