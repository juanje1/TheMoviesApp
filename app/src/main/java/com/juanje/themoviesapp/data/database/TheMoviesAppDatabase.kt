package com.juanje.themoviesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.daos.UserDao
import com.juanje.themoviesapp.data.database.entities.MovieEntity
import com.juanje.themoviesapp.data.database.entities.UserEntity

@Database(entities = [UserEntity::class, MovieEntity::class], version = 2)
abstract class TheMoviesAppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
}