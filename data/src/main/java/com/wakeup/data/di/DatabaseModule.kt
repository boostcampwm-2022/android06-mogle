package com.wakeup.data.di

import android.content.Context
import androidx.room.Room
import com.wakeup.data.database.MogleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): MogleDatabase = Room
        .databaseBuilder(context, MogleDatabase::class.java, "mogle.db")
        .build()

    @Singleton
    @Provides
    fun provideMomentDao(mogleDatabase: MogleDatabase) = mogleDatabase.momentDao()
}