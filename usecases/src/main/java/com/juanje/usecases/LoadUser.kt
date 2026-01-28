package com.juanje.usecases

import com.juanje.domain.dataclasses.User
import com.juanje.domain.repositories.UserRepository
import javax.inject.Inject

class LoadUser @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend fun invokeGetUser(email: String, password: String) =
        userRepository.getUser(email, password)

    suspend fun invokeExistsUserName(userName: String) =
        userRepository.existsUserName(userName)

    suspend fun invokeExistsEmail(email: String) =
        userRepository.existsEmail(email)

    suspend fun invokeInsertUser(user: User) =
        userRepository.insertUser(user)
}