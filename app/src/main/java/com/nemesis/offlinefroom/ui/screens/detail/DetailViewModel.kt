package com.nemesis.offlinefroom.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.nemesis.offlinefroom.domain.model.Character
import com.nemesis.offlinefroom.domain.usecase.GetCharacterDetailUseCase
import com.nemesis.offlinefroom.ui.navigation.DetailRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val characterId: Int = savedStateHandle.toRoute<DetailRoute>().characterId

    private val characterFlow: Flow<Character?> = getCharacterDetailUseCase(characterId)

    val uiState: StateFlow<DetailUiState> = characterFlow
        .map<Character?, DetailUiState> { character -> character.toUiState() }
        .catch { emit(DetailUiState.Error(LOCAL_ERROR_MESSAGE)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = DetailUiState.Loading
        )

    private fun Character?.toUiState(): DetailUiState =
        if (this != null) {
            DetailUiState.Success(this)
        } else {
            DetailUiState.Error(NOT_FOUND_MESSAGE)
        }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val NOT_FOUND_MESSAGE = "No se encontró el personaje en el dispositivo."
        const val LOCAL_ERROR_MESSAGE = "No se pudieron leer los datos guardados en el dispositivo."
    }
}