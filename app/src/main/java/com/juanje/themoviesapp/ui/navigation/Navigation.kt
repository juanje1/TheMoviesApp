package com.juanje.themoviesapp.ui.navigation

import androidx.compose.runtime.Composable
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
    val navToLoginRegistered = navToLoginRegistered(navController)
    val navToRegister = navToRegister(navController)
    val navToHome = navToHome(navController)
    val navToDetail = navToDetail(navController)

    NavHost(
        navController = navController,
        startDestination = Screen.Login(false)
    ) {
        composable<Screen.Login> {
            LoginScreen (
                onHome = navToHome,
                onRegister = navToRegister
            )
        }
        composable<Screen.Register> {
            RegisterScreen(
                onRegister = navToLoginRegistered,
                onLogin = navToLogin
            )
        }
        composable<Screen.Home> {
            HomeScreen(
                onLogin = navToLogin,
                onDetail = navToDetail
            )
        }
        composable<Screen.Detail> {
            DetailScreen(
                navController = navController,
                onLogin = navToLogin
            )
        }
    }
}