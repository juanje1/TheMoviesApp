package com.juanje.usecases

import com.juanje.data.repositories.UserRepository
import com.juanje.domain.User

class LoadUser(private val userRepository: UserRepository) {

    suspend fun invokeGetUser(email: String, password: String) =
        userRepository.getUser(email, password)

    suspend fun invokeExistsUserName(userName: String) =
        userRepository.existsUserName(userName)

    suspend fun invokeExistsEmail(email: String) =
        userRepository.existsEmail(email)

    suspend fun invokeInsertUser(user: User) =
        userRepository.insertUser(user)
}