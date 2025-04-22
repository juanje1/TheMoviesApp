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

    NavHost(
        navController = navController,
        startDestination = NavItem.Login.route
    ) {
        composable(NavItem.Login) {
            LoginScreen (
                onLoginClick = {
                    navController.navigate(NavItem.Home.createRoute(it.user!!.userName))
                },
                onRegisterClick = {
                    navController.navigate(NavigationRoutes.Register)
                }
            )
        }
        composable(NavItem.Register) {
            RegisterScreen(
                onClickRegistered = {
                    navController.navigate(NavigationRoutes.Login)
                },
                onClickCancel = {
                    navController.popBackStack()
                }
            )
        }
        composable(NavItem.Home) { backStackEntry ->
            HomeScreen(
                onClickMovie = {
                    navController.navigate(NavItem.Detail.createRoute(it.userName, it.id))
                },
                onClickBack = {
                    navController.popBackStack()
                },
                userName = backStackEntry.findArg(NavArg.UserName)
            )
        }
        composable(NavItem.Detail) { backStackEntry ->
            DetailScreen(
                onClickBack = {
                    navController.popBackStack()
                },
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