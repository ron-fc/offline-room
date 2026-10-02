package com.nemesis.offlinefroom.domain.usecase

import androidx.paging.PagingData
import com.nemesis.offlinefroom.domain.model.Character
import com.nemesis.offlinefroom.domain.repository.CharacterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<PagingData<Character>> = repository.getPagingCharacters()
}