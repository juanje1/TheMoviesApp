package com.juanje.themoviesapp.ui.navigation

import androidx.navigation.NavHostController

fun navToLogin(navController: NavHostController): () -> Unit = {
    navController.popBackStack()
    navController.navigate(Screen.Login(registered = false))
}

fun navToLoginRegistered(navController: NavHostController): () -> Unit = {
    navController.popBackStack()
    navController.navigate(Screen.Login(registered = true))
}

fun navToRegister(navController: NavHostController): () -> Unit = {
    navController.popBackStack()
    navController.navigate(Screen.Register)
}

fun navToHome(navController: NavHostController): (String) -> Unit = { userName ->
    navController.popBackStack()
    navController.navigate(Screen.Home(userName = userName))
}

fun navToDetail(navController: NavHostController): (String, Int) -> Unit = { userName, movieId ->
    navController.navigate(Screen.Detail(userName = userName, movieId = movieId))
}