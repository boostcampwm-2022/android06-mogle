package com.wakeup.data.di

import com.wakeup.data.repository.MomentRepositoryImpl
import com.wakeup.domain.repository.MomentRepository
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
}