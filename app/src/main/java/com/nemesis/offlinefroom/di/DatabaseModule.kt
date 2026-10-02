package com.nemesis.offlinefroom.di

import android.content.Context
import androidx.room.Room
import com.nemesis.offlinefroom.data.local.AppDatabase
import com.nemesis.offlinefroom.data.local.dao.CharacterDao
import com.nemesis.offlinefroom.data.local.dao.CharacterRemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideCharacterDao(database: AppDatabase): CharacterDao =
        database.characterDao()

    @Provides
    @Singleton
    fun provideCharacterRemoteKeysDao(database: AppDatabase): CharacterRemoteKeysDao =
        database.characterRemoteKeysDao()
}