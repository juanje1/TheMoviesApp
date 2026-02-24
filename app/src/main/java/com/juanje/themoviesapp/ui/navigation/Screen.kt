package com.juanje.themoviesapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data class Login(val registered: Boolean) : Screen

    @Serializable
    data object Register : Screen

    @Serializable
    data class Home(val userName: String) : Screen

    @Serializable
    data class Detail(val userName: String, val movieId: Int) : Screen
}