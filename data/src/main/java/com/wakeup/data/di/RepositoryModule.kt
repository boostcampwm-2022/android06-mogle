package com.wakeup.data.di

import com.wakeup.data.repository.PlaceSearchRepositoryImpl
import com.wakeup.domain.repository.PlaceSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindPlaceSearchRepository(impl: PlaceSearchRepositoryImpl): PlaceSearchRepository

}