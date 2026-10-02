package com.nemesis.offlinefroom.di

import com.nemesis.offlinefroom.data.local.dao.CharacterDao
import com.nemesis.offlinefroom.data.paging.CharacterRemoteMediator
import com.nemesis.offlinefroom.data.repository.CharacterRepositoryImpl
import com.nemesis.offlinefroom.domain.repository.CharacterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCharacterRepository(
        dao: CharacterDao,
        remoteMediator: CharacterRemoteMediator
    ): CharacterRepository = CharacterRepositoryImpl(
        dao = dao,
        remoteMediator = remoteMediator
    )
}