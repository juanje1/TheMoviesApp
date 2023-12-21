package com.juanje.themoviesapp.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem(
    val baseRoute: String,
    private val navArgs: List<NavArg> = emptyList()
) {
    val route = run {
        val argKeys = navArgs.map { "{${it.key}}" }
        listOf(baseRoute)
            .plus(argKeys)
            .joinToString("/")
    }
    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }

    object Login: NavItem(NavigationRoutes.Login)
    object Register: NavItem(NavigationRoutes.Register)
    object Home: NavItem(NavigationRoutes.Home, listOf(NavArg.UserName)) {
        fun createRoute(userName: String) = "$baseRoute/$userName"
    }
    object Detail: NavItem(NavigationRoutes.Detail, listOf(NavArg.UserName, NavArg.MovieId)) {
        fun createRoute(userName: String, movieId: Int) = "$baseRoute/$userName/$movieId"
    }
}

enum class NavArg(val key: String, val navType: NavType<*>, val type: String) {
    UserName(NavigationArgs.UserName, NavType.StringType, NavigationTypes.String),
    MovieId(NavigationArgs.MovieId, NavType.IntType, NavigationTypes.Int)
}