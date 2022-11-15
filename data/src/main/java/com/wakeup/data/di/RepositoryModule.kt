package com.wakeup.data.di

import com.wakeup.data.repository.moment.MomentRepositoryImpl
import com.wakeup.data.source.local.moment.MomentLocalDataSource
import com.wakeup.domain.repository.MomentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {
    @Provides
    fun provideMomentRepository(
        momentLocalDataSource: MomentLocalDataSource
    ): MomentRepository = MomentRepositoryImpl(momentLocalDataSource)
}