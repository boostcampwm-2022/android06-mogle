package com.wakeup.data.di

import com.wakeup.data.repository.PlaceSearchRepositoryImpl
import com.wakeup.data.repository.moment.MomentRepositoryImpl
import com.wakeup.domain.repository.MomentRepository
import com.wakeup.domain.repository.PlaceSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindMomentRepository(
        momentRepositoryImpl: MomentRepositoryImpl
    ): MomentRepository

    @Binds
    fun bindPlaceSearchRepository(
        placeSearchRepositoryImpl: PlaceSearchRepositoryImpl
    ): PlaceSearchRepository
}