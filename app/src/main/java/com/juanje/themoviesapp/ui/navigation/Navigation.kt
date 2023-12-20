package com.juanje.themoviesapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.juanje.themoviesapp.ui.screens.detail.DetailScreen
import com.juanje.themoviesapp.ui.screens.home.HomeScreen
import com.juanje.themoviesapp.ui.screens.login.LoginScreen
import com.juanje.themoviesapp.ui.screens.register.RegisterScreen
import com.juanje.themoviesapp.ui.theme.TheMoviesAppTheme
import java.lang.IllegalArgumentException

@ExperimentalCoilApi
@Composable
fun Navigation() {
    val navController = rememberNavController()

    TheMoviesAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            NavHost(
                navController = navController,
                startDestination = NavItem.Login.route
            ) {
                composable(NavItem.Login) {
                    LoginScreen(navController)
                }
                composable(NavItem.Register) {
                    RegisterScreen(navController)
                }
                composable(NavItem.Home) { backStackEntry ->
                    HomeScreen(
                        navController = navController,
                        userName = backStackEntry.findArg(NavArg.UserName)
                    )
                }
                composable(NavItem.Detail) { backStackEntry ->
                    DetailScreen(
                        navController = navController,
                        userName = backStackEntry.findArg(NavArg.UserName),
                        movieId = backStackEntry.findArg(NavArg.MovieId)
                    )
                }
            }
        }
    }
}

private fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = navItem.route,
        arguments = navItem.args,
    ) {
        content(it)
    }
}

private inline fun <reified T> NavBackStackEntry.findArg(arg: NavArg): T {
    val value: Any? = when (arg.type) {
        NavigationTypes.String -> arguments?.getString(arg.key)
        NavigationTypes.Int -> arguments?.getInt(arg.key)
        NavigationTypes.Long -> arguments?.getLong(arg.key)
        NavigationTypes.Float -> arguments?.getFloat(arg.key)
        NavigationTypes.Boolean -> arguments?.getBoolean(arg.key)
        else -> throw IllegalArgumentException()
    }
    requireNotNull(value)
    return value as T
}