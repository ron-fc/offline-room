package com.nemesis.offlinefroom.domain.usecase

import com.nemesis.offlinefroom.domain.model.Character
import com.nemesis.offlinefroom.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<List<Character>> = repository.getCharacters()
}