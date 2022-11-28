package com.wakeup.data.di

import android.content.Context
import androidx.room.Room
import com.wakeup.data.database.MogleDatabase
import com.wakeup.data.database.dao.GlobeDao
import com.wakeup.data.database.dao.MomentDao
import com.wakeup.data.util.InternalFileUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): MogleDatabase = Room
        .databaseBuilder(context, MogleDatabase::class.java, "mogle.db")
        .addCallback(MogleDatabase.callback)
        .build()

    @Singleton
    @Provides
    fun provideMomentDao(
        mogleDatabase: MogleDatabase,
    ): MomentDao = mogleDatabase.momentDao()

    @Singleton
    @Provides
    fun provideGlobeDao(
        mogleDatabase: MogleDatabase,
    ): GlobeDao = mogleDatabase.globeDao()

    @Singleton
    @Provides
    fun provideInternalFileUtil(
        @ApplicationContext context: Context,
    ): InternalFileUtil = InternalFileUtil(context, Dispatchers.IO)
}