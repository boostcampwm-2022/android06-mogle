package com.wakeup.data.di

import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.source.local.moment.MomentLocalDataSourceImpl
import com.wakeup.data.source.local.moment.MomentLocalPagingSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideMomentLocalDataSource(
        momentDao: MomentDao
    ): MomentLocalDataSource = MomentLocalDataSourceImpl(momentDao)
}