package com.nemesis.offlinefroom.domain.repository

import androidx.paging.PagingData
import com.nemesis.offlinefroom.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    /** Emite los personajes paginados; Room es la única fuente de verdad. */
    fun getPagingCharacters(): Flow<PagingData<Character>>

    /** Emite el personaje con el id dado, o null si no existe en la base local. */
    fun getCharacterById(id: Int): Flow<Character?>
}