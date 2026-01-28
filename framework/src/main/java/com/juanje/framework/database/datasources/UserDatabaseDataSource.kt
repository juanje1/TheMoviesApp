package com.juanje.framework.database.datasources

import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.domain.IoDispatcher
import com.juanje.domain.dataclasses.User
import com.juanje.framework.database.daos.UserDao
import com.juanje.framework.database.dataclasses.toUser
import com.juanje.framework.database.dataclasses.toUserDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserDatabaseDataSource @Inject constructor(
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