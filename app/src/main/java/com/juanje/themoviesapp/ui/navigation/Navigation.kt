package com.juanje.themoviesapp.ui.navigation

import androidx.compose.runtime.Composable
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

@ExperimentalCoilApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navToLogin = navToLogin(navController)
    val navToRegister = navToRegister(navController)
    val navToHome = navToHome(navController)
    val navToDetail = navToDetail(navController)

    NavHost(
        navController = navController,
        startDestination = NavItem.Login.route
    ) {
        composable(NavItem.Login) {
            LoginScreen (
                onHomeClick = navToHome,
                onRegisterClick = navToRegister
            )
        }
        composable(NavItem.Register) {
            RegisterScreen(
                onRegisteredClick = navToLogin,
                onLoginClick = navToLogin
            )
        }
        composable(NavItem.Home) { backStackEntry ->
            HomeScreen(
                onLoginClick = navToLogin,
                onDetailClick = navToDetail,
                userName = backStackEntry.findArg(NavArg.UserName)
            )
        }
        composable(NavItem.Detail) { backStackEntry ->
            DetailScreen(
                onHomeClick = navToHome,
                userName = backStackEntry.findArg(NavArg.UserName),
                movieId = backStackEntry.findArg(NavArg.MovieId)
            )
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