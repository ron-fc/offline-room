package com.nemesis.offlinefroom.ui.screens.detail

import com.nemesis.offlinefroom.domain.model.Character

sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val character: Character) : DetailUiState
    data class Error(val message: String) : DetailUiState
}