package com.nemesis.offlinefroom.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nemesis.offlinefroom.domain.model.Character
import com.nemesis.offlinefroom.domain.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

@HiltViewModel
class HomeViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    val charactersPagingFlow: Flow<PagingData<Character>> =
        getCharactersUseCase().cachedIn(viewModelScope)

    fun toUserMessage(error: Throwable): String = when (error) {
        is HttpException ->
            "El servidor no respondió correctamente (código ${error.code()}). Inténtalo de nuevo."
        is IOException ->
            "No se pudo conectar. Revisa tu conexión a internet e inténtalo de nuevo."
        else ->
            "Ocurrió un error inesperado. Inténtalo de nuevo."
    }
}