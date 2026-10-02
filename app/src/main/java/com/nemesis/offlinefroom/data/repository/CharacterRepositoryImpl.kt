package com.nemesis.offlinefroom.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.nemesis.offlinefroom.data.local.dao.CharacterDao
import com.nemesis.offlinefroom.data.mapper.toDomain
import com.nemesis.offlinefroom.data.paging.CharacterRemoteMediator
import com.nemesis.offlinefroom.domain.model.Character
import com.nemesis.offlinefroom.domain.repository.CharacterRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class CharacterRepositoryImpl(
    private val dao: CharacterDao,
    private val remoteMediator: CharacterRemoteMediator,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingCharacters(): Flow<PagingData<Character>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { dao.getPagingCharacters() }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }

    override fun getCharacterById(id: Int): Flow<Character?> =
        dao.getCharacterById(id)
            .map { entity -> entity?.toDomain() }
            .flowOn(ioDispatcher)

    private companion object {
        const val PAGE_SIZE = 20
    }
}