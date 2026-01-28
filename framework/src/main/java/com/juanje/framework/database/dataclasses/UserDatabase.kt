package com.juanje.framework.database.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juanje.domain.dataclasses.User

@Entity
data class UserDatabase(
    @PrimaryKey val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

fun UserDatabase.toUser() = User(
    userName = userName,
    firstName = firstName,
    lastName = lastName,
    email = email,
    password = password
)

fun User.toUserDatabase() = UserDatabase(
    userName = userName,
    firstName = firstName,
    lastName = lastName,
    email = email,
    password = password
)