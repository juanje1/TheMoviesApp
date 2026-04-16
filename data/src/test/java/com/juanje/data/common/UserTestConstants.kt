package com.juanje.data.common

import com.juanje.domain.dataclasses.User

internal object UserTestConstants {
    const val WRONG_EMAIL: String = "wrong@email.com"
    const val WRONG_PASSWORD: String = "1234"
}

fun createUser() = User(
    userName = "juanje",
    firstName = "Juan",
    lastName = "Bonito",
    email = "juanje@example.com",
    password = "password123"
)