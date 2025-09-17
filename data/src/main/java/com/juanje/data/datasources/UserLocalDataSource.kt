package com.juanje.data.datasources

import com.juanje.domain.User

interface UserLocalDataSource {

    suspend fun getUser(email: String, password: String): User?
    suspend fun existsUserName(userName: String): Boolean
    suspend fun existsEmail(email: String): Boolean
    suspend fun insertUser(user: User)
}