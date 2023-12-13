package com.juanje.themoviesapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.juanje.domain.User

@Entity
data class UserEntity(
    @PrimaryKey val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

fun UserEntity.toUser() = User(
    userName = userName,
    firstName = firstName,
    lastName = lastName,
    email = email,
    password = password
)

fun User.toUserEntity() = UserEntity(
    userName = userName,
    firstName = firstName,
    lastName = lastName,
    email = email,
    password = password
)