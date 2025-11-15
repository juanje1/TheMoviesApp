package com.juanje.themoviesapp.data.database.datasources

import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.domain.User
import com.juanje.themoviesapp.data.IoDispatcher
import com.juanje.themoviesapp.data.database.daos.UserDao
import com.juanje.themoviesapp.data.database.dataclasses.toUser
import com.juanje.themoviesapp.data.database.dataclasses.toUserDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserDatabaseDataSource(
    private val userDao: UserDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): UserLocalDataSource {

    override suspend fun getUser(email: String, password: String): User? =
        withContext(ioDispatcher) {
            userDao.getUser(email, password)?.toUser()
        }

    override suspend fun existsUserName(userName: String): Boolean =
        withContext(ioDispatcher) {
            userDao.existsUserName(userName)
        }

    override suspend fun existsEmail(email: String): Boolean =
        withContext(ioDispatcher) {
            userDao.existsEmail(email)
        }

    override suspend fun insertUser(user: User) =
        withContext(ioDispatcher) {
            userDao.insertUser(user.toUserDatabase())
        }
}