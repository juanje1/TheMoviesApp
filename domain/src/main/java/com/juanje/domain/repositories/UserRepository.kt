package com.juanje.domain.repositories

import com.juanje.domain.dataclasses.User

interface UserRepository {
    suspend fun getUser(email: String, password: String): User?
    suspend fun existsUserName(userName: String): Boolean
    suspend fun existsEmail(email: String): Boolean
    suspend fun insertUser(user: User)
}