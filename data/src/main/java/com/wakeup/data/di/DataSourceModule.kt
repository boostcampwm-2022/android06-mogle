package com.wakeup.data.di

import com.wakeup.data.source.local.globe.GlobeLocalDataSource
import com.wakeup.data.source.local.globe.GlobeLocalDataSourceImpl
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.source.local.moment.MomentLocalDataSourceImpl
import com.wakeup.data.source.local.picture.PictureLocalDataSource
import com.wakeup.data.source.local.picture.PictureLocalDataSourceImpl
import com.wakeup.data.source.local.xref.XRefLocalDataSource
import com.wakeup.data.source.local.xref.XRefLocalDataSourceImpl
import com.wakeup.data.source.remote.placeSearch.PlaceSearchRemoteDataSource
import com.wakeup.data.source.remote.placeSearch.PlaceSearchRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    fun bindMomentLocalDataSource(
        momentLocalDataSourceImpl: MomentLocalDataSourceImpl,
    ): MomentLocalDataSource

    @Binds
    fun bindPictureLocalDataSource(
        pictureLocalDataSourceImpl: PictureLocalDataSourceImpl,
    ): PictureLocalDataSource

    @Binds
    fun bindPlaceSearchRemoteDataSource(
        placeSearchRemoteDataSourceImpl: PlaceSearchRemoteDataSourceImpl,
    ): PlaceSearchRemoteDataSource

    @Binds
    fun bindGlobeLocalDataSource(
        globeLocalDataSourceImpl: GlobeLocalDataSourceImpl,
    ): GlobeLocalDataSource

    @Binds
    fun bindXRefLocalDataSource(
        xRefLocalDataSourceImpl: XRefLocalDataSourceImpl,
    ): XRefLocalDataSource
}