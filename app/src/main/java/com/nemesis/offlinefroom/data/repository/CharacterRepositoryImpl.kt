package com.nemesis.offlinefroom.data.repository

import com.nemesis.offlinefroom.data.local.dao.CharacterDao
import com.nemesis.offlinefroom.data.mapper.toDomain
import com.nemesis.offlinefroom.data.mapper.toEntity
import com.nemesis.offlinefroom.data.remote.api.RickAndMortyApi
import com.nemesis.offlinefroom.domain.model.Character
import com.nemesis.offlinefroom.domain.repository.CharacterRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CharacterRepositoryImpl(
    private val dao: CharacterDao,
    private val api: RickAndMortyApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CharacterRepository {

    override fun getCharacters(): Flow<List<Character>> =
        dao.getAllCharacters()
            .map { entities -> entities.map { it.toDomain() } }
            .flowOn(ioDispatcher)

    override suspend fun refreshCharacters(): Result<Unit> = withContext(ioDispatcher) {
        try {
            val response = api.getCharacters()
            dao.insertCharacters(response.results.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}