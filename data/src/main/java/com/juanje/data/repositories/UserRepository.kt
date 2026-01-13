package com.juanje.data.repositories

import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.data.utilities.safeCall
import com.juanje.domain.User

class UserRepository(private val userLocalDataSource: UserLocalDataSource) {

    suspend fun getUser(email: String, password: String): User? {
        return safeCall {
            userLocalDataSource.getUser(email, password)
        }
    }

    suspend fun existsUserName(userName: String): Boolean {
        return safeCall {
            userLocalDataSource.existsUserName(userName)
        }
    }

    suspend fun existsEmail(email: String): Boolean {
        return safeCall {
            userLocalDataSource.existsEmail(email)
        }
    }

    suspend fun insertUser(user: User) {
        return safeCall {
            userLocalDataSource.insertUser(user)
        }
    }
}