package com.nemesis.offlinefroom.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nemesis.offlinefroom.ui.screens.detail.DetailScreen
import com.nemesis.offlinefroom.ui.screens.home.HomeScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(
                viewModel = hiltViewModel(),
                onCharacterClick = { characterId ->
                    navController.navigate(DetailRoute(characterId))
                }
            )
        }

        composable<DetailRoute> {
            DetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}