package com.juanje.data.repositories

import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.domain.User

class UserRepository(private val userLocalDataSource: UserLocalDataSource) {

    suspend fun getUser(email: String, password: String) =
        userLocalDataSource.getUser(email, password)

    suspend fun existsUserName(userName: String) =
        userLocalDataSource.existsUserName(userName)

    suspend fun existsEmail(email: String) =
        userLocalDataSource.existsEmail(email)

    suspend fun insertUser(user: User) =
        userLocalDataSource.insertUser(user)
}