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
import com.juanje.themoviesapp.ui.navigation.Navigation.Login
import com.juanje.themoviesapp.ui.navigation.Navigation.MovieDetailsArgs.MovieId
import com.juanje.themoviesapp.ui.navigation.Navigation.Register
import com.juanje.themoviesapp.ui.navigation.Navigation.UserNameArgs.UserName
import com.juanje.themoviesapp.ui.screens.detail.DetailScreen
import com.juanje.themoviesapp.ui.screens.home.HomeScreen
import com.juanje.themoviesapp.ui.screens.login.LoginScreen
import com.juanje.themoviesapp.ui.screens.register.RegisterScreen
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
            NavHost(
                navController = navController,
                startDestination = Login
            ) {
                composable(Login) {
                    LoginScreen(navController)
                }
                composable(Register) {
                    RegisterScreen(navController)
                }
                composable(
                    route = "$Home/{$UserName}",
                    arguments = listOf(
                        navArgument(UserName) { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val userName = backStackEntry.arguments?.getString(UserName)
                    requireNotNull(userName)
                    HomeScreen(navController, userName)
                }
                composable(
                    route = "$Detail/{$UserName}/{$MovieId}",
                    arguments = listOf(
                        navArgument(UserName) { type = NavType.StringType },
                        navArgument(MovieId) { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val userName = backStackEntry.arguments?.getString(UserName)
                    val movieId = backStackEntry.arguments?.getInt(MovieId)
                    requireNotNull(userName)
                    requireNotNull(movieId)
                    DetailScreen(userName, movieId)
                }
            }
        }
    }
}