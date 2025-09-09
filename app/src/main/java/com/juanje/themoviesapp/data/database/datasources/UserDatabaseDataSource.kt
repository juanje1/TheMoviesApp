package com.juanje.themoviesapp.data.database.datasources

import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.domain.User
import com.juanje.themoviesapp.data.database.daos.UserDao
import com.juanje.themoviesapp.data.database.dataclasses.toUser
import com.juanje.themoviesapp.data.database.dataclasses.toUserDatabase

class UserDatabaseDataSource(private val userDao: UserDao): UserLocalDataSource {

    override suspend fun getUser(email: String, password: String): User? =
        userDao.getUser(email, password)?.toUser()

    override suspend fun existsUserName(userName: String): Boolean =
        userDao.existsUserName(userName)

    override suspend fun existsEmail(email: String): Boolean =
        userDao.existsEmail(email)

    override suspend fun insertUser(user: User) =
        userDao.insertUser(user.toUserDatabase())
}