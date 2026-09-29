#!/usr/bin/env kotlin

package com.nemesis.offlinefroom.domain.repository

import com.nemesis.offlinefroom.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    /** Emite la lista de personajes desde la fuente local/reactiva. */
    fun getCharacters(): Flow<List<Character>>

    /** Fuerza la sincronización desde la red hacia la base de datos local. */
    suspend fun refreshCharacters(): Result<Unit>
}