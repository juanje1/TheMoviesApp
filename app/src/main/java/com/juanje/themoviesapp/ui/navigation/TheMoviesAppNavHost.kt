package com.juanje.themoviesapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.juanje.themoviesapp.ui.navigation.Navigation.Detail
import com.juanje.themoviesapp.ui.navigation.Navigation.Home
import com.juanje.themoviesapp.ui.navigation.Navigation.MovieDetailArgs.MovieId
import com.juanje.themoviesapp.ui.screens.detail.DetailScreen
import com.juanje.themoviesapp.ui.screens.home.HomeScreen
import com.juanje.themoviesapp.ui.theme.TheMoviesAppTheme

@ExperimentalCoilApi
@Composable
fun TheMoviesAppNavHost() {
    val navController = rememberNavController()

    TheMoviesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            NavHost(navController = navController, startDestination = Home) {
                composable(Home) {
                    HomeScreen(navController)
                }
                composable(
                    route = "$Detail/{$MovieId}",
                    arguments = listOf(
                        navArgument(MovieId) { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt(MovieId)
                    requireNotNull(id)
                    DetailScreen(id)
                }
            }
        }
    }
}