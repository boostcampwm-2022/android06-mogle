package com.wakeup.data.di

import com.wakeup.data.source.remote.PlaceSearch.PlaceSearchRemoteDataSource
import com.wakeup.data.source.remote.PlaceSearch.PlaceSearchRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class TmpDatasourceModule {

    @Binds
    abstract fun bindPlaceDataSource(impl: PlaceSearchRemoteDataSourceImpl): PlaceSearchRemoteDataSource

}