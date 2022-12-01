package com.wakeup.data.di

import com.wakeup.data.repository.GlobeRepositoryImpl
import com.wakeup.data.repository.MomentRepositoryImpl
import com.wakeup.data.repository.PictureRepositoryImpl
import com.wakeup.data.repository.PlaceSearchRepositoryImpl
import com.wakeup.data.repository.RelationRepositoryImpl
import com.wakeup.domain.repository.GlobeRepository
import com.wakeup.domain.repository.MomentRepository
import com.wakeup.domain.repository.PictureRepository
import com.wakeup.domain.repository.PlaceSearchRepository
import com.wakeup.domain.repository.RelationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindMomentRepository(
        momentRepositoryImpl: MomentRepositoryImpl,
    ): MomentRepository

    @Binds
    fun bindPictureRepository(
        pictureRepositoryImpl: PictureRepositoryImpl,
    ): PictureRepository

    @Binds
    fun bindPlaceSearchRepository(
        placeSearchRepositoryImpl: PlaceSearchRepositoryImpl,
    ): PlaceSearchRepository

    @Binds
    fun bindGlobeRepository(
        globeRepositoryImpl: GlobeRepositoryImpl,
    ): GlobeRepository

    @Binds
    fun bindRefRepository(
        refRepositoryImpl: RelationRepositoryImpl,
    ): RelationRepository
}