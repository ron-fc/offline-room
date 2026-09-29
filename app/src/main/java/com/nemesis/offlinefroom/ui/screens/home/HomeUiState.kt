package com.nemesis.offlinefroom.ui.screens.home

import com.nemesis.offlinefroom.domain.model.Character

sealed interface HomeUiState {

    data object Loading : HomeUiState

    data class Success(
        val characters: List<Character>,
        val isOffline: Boolean = false
    ) : HomeUiState

    data class Error(val message: String) : HomeUiState
}