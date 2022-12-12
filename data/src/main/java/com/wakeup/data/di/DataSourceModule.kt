package com.wakeup.data.di

import com.wakeup.data.source.local.globe.GlobeLocalDataSource
import com.wakeup.data.source.local.globe.GlobeLocalDataSourceImpl
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.data.source.local.moment.MomentLocalDataSourceImpl
import com.wakeup.data.source.local.xref.XRefLocalDataSource
import com.wakeup.data.source.local.xref.XRefLocalDataSourceImpl
import com.wakeup.data.source.remote.imageSearch.ImageSearchRemoteDataSource
import com.wakeup.data.source.remote.imageSearch.ImageSearchRemoteDataSourceImpl
import com.wakeup.data.source.remote.placeSearch.PlaceSearchRemoteDataSource
import com.wakeup.data.source.remote.placeSearch.PlaceSearchRemoteDataSourceImpl
import com.wakeup.data.source.remote.weather.WeatherRemoteDataSource
import com.wakeup.data.source.remote.weather.WeatherRemoteDataSourceImpl
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
    fun bindPlaceSearchRemoteDataSource(
        placeSearchRemoteDataSourceImpl: PlaceSearchRemoteDataSourceImpl,
    ): PlaceSearchRemoteDataSource

    @Binds
    fun bindGlobeLocalDataSource(
        globeLocalDataSourceImpl: GlobeLocalDataSourceImpl,
    ): GlobeLocalDataSource

    @Binds
    fun bindWeatherRemoteDataSource(
        weatherRemoteDataSourceImpl: WeatherRemoteDataSourceImpl,
    ): WeatherRemoteDataSource

    @Binds
    fun bindXRefLocalDataSource(
        xRefLocalDataSourceImpl: XRefLocalDataSourceImpl,
    ): XRefLocalDataSource

    @Binds
    fun bindImageSearchRemoteDataSource(
        imageSearchRemoteDataSourceImpl: ImageSearchRemoteDataSourceImpl,
    ): ImageSearchRemoteDataSource
}