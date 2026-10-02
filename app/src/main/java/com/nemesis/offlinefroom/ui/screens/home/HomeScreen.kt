package com.nemesis.offlinefroom.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.nemesis.offlinefroom.domain.model.Character
import com.nemesis.offlinefroom.ui.components.CharacterCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyPagingItems = viewModel.charactersPagingFlow.collectAsLazyPagingItems()
    val refreshState = lazyPagingItems.loadState.refresh
    val isEmpty = lazyPagingItems.itemCount == 0

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Rick & Morty (Offline-First)") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                isEmpty && refreshState is LoadState.Loading -> LoadingContent()

                isEmpty && refreshState is LoadState.Error -> ErrorContent(
                    message = viewModel.toUserMessage(refreshState.error),
                    onRetry = lazyPagingItems::refresh
                )

                else -> CharacterListContent(
                    lazyPagingItems = lazyPagingItems,
                    isOffline = refreshState is LoadState.Error,
                    isRefreshing = refreshState is LoadState.Loading,
                    onCharacterClick = onCharacterClick
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Algo salió mal",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) {
            Text(text = "Reintentar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListContent(
    lazyPagingItems: LazyPagingItems<Character>,
    isOffline: Boolean,
    isRefreshing: Boolean,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val appendState = lazyPagingItems.loadState.append

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = lazyPagingItems::refresh,
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (isOffline) {
                OfflineBanner()
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (lazyPagingItems.itemCount == 0) {
                    item(key = "empty") {
                        Text(
                            text = "No hay personajes disponibles. Desliza hacia abajo para actualizar.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                        )
                    }
                } else {
                    items(
                        count = lazyPagingItems.itemCount,
                        key = lazyPagingItems.itemKey { character -> character.id }
                    ) { index ->
                        val character = lazyPagingItems[index]
                        if (character != null) {
                            Box(
                                modifier = Modifier.clickable {
                                    onCharacterClick(character.id)
                                }
                            ) {
                                CharacterCard(character = character)
                            }
                        }
                    }
                }

                if (appendState is LoadState.Loading) {
                    item(key = "append_loading") {
                        AppendLoadingItem()
                    }
                }

                if (appendState is LoadState.Error) {
                    item(key = "append_error") {
                        AppendErrorItem(onRetry = lazyPagingItems::retry)
                    }
                }
            }
        }
    }
}

@Composable
private fun AppendLoadingItem(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun AppendErrorItem(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No se pudieron cargar más personajes.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "Reintentar")
        }
    }
}

@Composable
private fun OfflineBanner(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        Text(
            text = "Modo sin conexión - Mostrando datos guardados",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}