package com.juanje.themoviesapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.juanje.themoviesapp.data.database.dataclasses.UserDatabase

@Dao
interface UserDao {

    @Query("SELECT * FROM UserDatabase WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): UserDatabase?

    @Query("SELECT EXISTS (SELECT * FROM UserDatabase WHERE userName = :userName)")
    suspend fun existsUserName(userName: String): Boolean

    @Query("SELECT EXISTS (SELECT * FROM UserDatabase WHERE email = :email)")
    suspend fun existsEmail(email: String): Boolean

    @Insert
    suspend fun insertUser(user: UserDatabase)
}