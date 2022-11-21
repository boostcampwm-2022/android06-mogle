package com.wakeup.data.di

import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.source.local.moment.MomentLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindMomentLocalDataSource(
        momentLocalDataSourceImpl: MomentLocalDataSourceImpl
    ): MomentLocalDataSource
}