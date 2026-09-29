package com.nemesis.offlinefroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nemesis.offlinefroom.data.local.AppDatabase
import com.nemesis.offlinefroom.data.remote.client.RetrofitClient
import com.nemesis.offlinefroom.data.repository.CharacterRepositoryImpl
import com.nemesis.offlinefroom.domain.repository.CharacterRepository
import com.nemesis.offlinefroom.domain.usecase.GetCharactersUseCase
import com.nemesis.offlinefroom.ui.screens.home.HomeScreen
import com.nemesis.offlinefroom.ui.screens.home.HomeViewModel
import com.nemesis.offlinefroom.ui.theme.OfflineFRoomAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CharacterRepositoryImpl(
            dao = database.characterDao(),
            api = RetrofitClient.api
        )
        HomeViewModelFactory(
            getCharactersUseCase = GetCharactersUseCase(repository),
            repository = repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfflineFRoomAppTheme {
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}

private class HomeViewModelFactory(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val repository: CharacterRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            "ViewModel desconocido: ${modelClass.name}"
        }
        return HomeViewModel(getCharactersUseCase, repository) as T
    }
}