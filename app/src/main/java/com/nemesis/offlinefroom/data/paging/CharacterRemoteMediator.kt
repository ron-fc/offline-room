package com.nemesis.offlinefroom.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.nemesis.offlinefroom.data.local.AppDatabase
import com.nemesis.offlinefroom.data.local.entity.CharacterEntity
import com.nemesis.offlinefroom.data.local.entity.CharacterRemoteKeysEntity
import com.nemesis.offlinefroom.data.mapper.toEntity
import com.nemesis.offlinefroom.data.remote.api.RickAndMortyApi
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator @Inject constructor(
    private val database: AppDatabase,
    private val api: RickAndMortyApi
) : RemoteMediator<Int, CharacterEntity>() {

    private val characterDao = database.characterDao()
    private val remoteKeysDao = database.characterRemoteKeysDao()

    // Al abrir la app se intenta sincronizar; si falla, Room sigue mostrando lo guardado.
    override suspend fun initialize(): InitializeAction =
        InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE

            // La lista siempre empieza en la página 1, no hay nada antes.
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = false)
                val remoteKeys = remoteKeysDao.getRemoteKeysByCharacterId(lastItem.id)
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
            }
        }

        return try {
            val response = api.getCharacters(page = page)
            val characters = response.results.map { it.toEntity() }
            val endOfPaginationReached = response.info.next == null || characters.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    characterDao.clearAllCharacters()
                }

                val prevKey = if (page == STARTING_PAGE) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                remoteKeysDao.insertAll(
                    characters.map { character ->
                        CharacterRemoteKeysEntity(
                            characterId = character.id,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }
                )
                characterDao.insertCharacters(characters)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private companion object {
        const val STARTING_PAGE = 1
    }
}