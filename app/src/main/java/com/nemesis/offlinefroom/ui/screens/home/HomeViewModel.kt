
package com.nemesis.offlinefroom.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nemesis.offlinefroom.domain.model.Character
import com.nemesis.offlinefroom.domain.repository.CharacterRepository
import com.nemesis.offlinefroom.domain.usecase.GetCharactersUseCase
import java.io.IOException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    getCharactersUseCase: GetCharactersUseCase,
    private val repository: CharacterRepository
) : ViewModel() {

    private sealed interface RefreshState {
        data object InProgress : RefreshState
        data object Idle : RefreshState
        data class Failed(val message: String) : RefreshState
    }

    private val refreshState = MutableStateFlow<RefreshState>(RefreshState.InProgress)

    private var refreshJob: Job? = null

    /**
     * Combina la fuente de verdad local (Room) con el estado de la sincronización de red:
     * - Room vacío + sincronizando        -> Loading
     * - Room vacío + fallo de red         -> Error
     * - Room con datos + fallo de red     -> Success(isOffline = true)
     * - Room con datos + red OK           -> Success(isOffline = false)
     */
    val uiState: StateFlow<HomeUiState> = combine(
        getCharactersUseCase()
            .map { characters -> Result.success(characters) }
            .catch { error -> emit(Result.failure(error)) },
        refreshState
    ) { charactersResult, refresh ->
        buildUiState(charactersResult, refresh)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = HomeUiState.Loading
    )

    /** Indica si hay una sincronización de red en curso (para pull-to-refresh). */
    val isRefreshing: StateFlow<Boolean> = refreshState
        .map { it is RefreshState.InProgress }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = true
        )

    init {
        refresh()
    }

    /** Reintenta la sincronización. Ignora llamadas mientras haya una en curso. */
    fun refresh() {
        if (refreshJob?.isActive == true) return

        refreshJob = viewModelScope.launch {
            refreshState.value = RefreshState.InProgress
            refreshState.value = repository.refreshCharacters().fold(
                onSuccess = { RefreshState.Idle },
                onFailure = { error -> RefreshState.Failed(error.toUserMessage()) }
            )
        }
    }

    private fun buildUiState(
        charactersResult: Result<List<Character>>,
        refresh: RefreshState
    ): HomeUiState {
        val characters = charactersResult.getOrNull()
            ?: return HomeUiState.Error(LOCAL_ERROR_MESSAGE)

        return if (characters.isNotEmpty()) {
            HomeUiState.Success(
                characters = characters,
                isOffline = refresh is RefreshState.Failed
            )
        } else {
            when (refresh) {
                RefreshState.InProgress -> HomeUiState.Loading
                RefreshState.Idle -> HomeUiState.Success(characters = emptyList())
                is RefreshState.Failed -> HomeUiState.Error(refresh.message)
            }
        }
    }

    private fun Throwable.toUserMessage(): String = when (this) {
        is HttpException ->
            "El servidor no respondió correctamente (código ${code()}). Inténtalo de nuevo."
        is IOException ->
            "No se pudo conectar. Revisa tu conexión a internet e inténtalo de nuevo."
        else ->
            "Ocurrió un error inesperado. Inténtalo de nuevo."
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val LOCAL_ERROR_MESSAGE = "No se pudieron leer los datos guardados en el dispositivo."
    }
}