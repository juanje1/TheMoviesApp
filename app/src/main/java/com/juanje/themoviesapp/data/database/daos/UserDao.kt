package com.juanje.themoviesapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.juanje.themoviesapp.data.database.entities.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM UserEntity WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): UserEntity?

    @Query("SELECT EXISTS (SELECT * FROM UserEntity WHERE userName = :userName)")
    suspend fun existsUserName(userName: String): Boolean

    @Query("SELECT EXISTS (SELECT * FROM UserEntity WHERE email = :email)")
    suspend fun existsEmail(email: String): Boolean

    @Insert
    suspend fun insertUser(user: UserEntity)
}