package com.nemesis.offlinefroom.domain.usecase

import com.nemesis.offlinefroom.domain.model.Character
import com.nemesis.offlinefroom.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterDetailUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(id: Int): Flow<Character?> = repository.getCharacterById(id)
}