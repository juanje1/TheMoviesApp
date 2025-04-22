package com.juanje.themoviesapp.ui.navigation

import androidx.navigation.NavHostController

fun navToLogin(navController: NavHostController): () -> Unit = {
    navController.popBackStack()
    navController.navigate(NavigationRoutes.Login)
}

fun navToRegister(navController: NavHostController): () -> Unit = {
    navController.popBackStack()
    navController.navigate(NavigationRoutes.Register)
}

fun navToHome(navController: NavHostController): (String) -> Unit = { userName ->
    navController.popBackStack()
    navController.navigate(NavItem.Home.createRoute(
        userName = userName
    ))
}

fun navToDetail(navController: NavHostController): (String, Int) -> Unit = { userName, movieId ->
    navController.popBackStack()
    navController.navigate(NavItem.Detail.createRoute(
        userName = userName,
        movieId = movieId
    ))
}