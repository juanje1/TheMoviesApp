package com.juanje.themoviesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.daos.UserDao
import com.juanje.themoviesapp.data.database.dataclasses.MovieDatabase
import com.juanje.themoviesapp.data.database.dataclasses.UserDatabase

@Database(entities = [UserDatabase::class, MovieDatabase::class], version = 5)
abstract class TheMoviesAppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
}