package com.juanje.themoviesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juanje.themoviesapp.data.database.daos.FavoriteDao
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.daos.UserDao
import com.juanje.themoviesapp.data.database.dataclasses.FavoriteDatabase
import com.juanje.themoviesapp.data.database.dataclasses.MovieDatabase
import com.juanje.themoviesapp.data.database.dataclasses.UserDatabase

@Database(entities = [UserDatabase::class, MovieDatabase::class, FavoriteDatabase::class], version = 7)
abstract class TheMoviesAppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
    abstract fun favoriteDao(): FavoriteDao
}