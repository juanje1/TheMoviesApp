package com.juanje.themoviesapp.ui.screens.common

import com.juanje.domain.dataclasses.User

object UserMother {

    fun createUser(
        userName: String = "jj",
        firstName: String = "Juan",
        lastName: String = "Bon",
        email: String = "test@example.com",
        password: String = "12345678"
    ) = User(userName, firstName, lastName, email, password)
}