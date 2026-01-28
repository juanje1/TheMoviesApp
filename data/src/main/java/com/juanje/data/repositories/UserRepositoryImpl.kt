package com.juanje.data.repositories

import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.data.common.safeCall
import com.juanje.domain.dataclasses.User
import com.juanje.domain.repositories.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
): UserRepository {

    override suspend fun getUser(email: String, password: String): User? {
        return safeCall {
            userLocalDataSource.getUser(email, password)
        }
    }

    override suspend fun existsUserName(userName: String): Boolean {
        return safeCall {
            userLocalDataSource.existsUserName(userName)
        }
    }

    override suspend fun existsEmail(email: String): Boolean {
        return safeCall {
            userLocalDataSource.existsEmail(email)
        }
    }

    override suspend fun insertUser(user: User) {
        return safeCall {
            userLocalDataSource.insertUser(user)
        }
    }
}