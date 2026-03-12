package com.juanje.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juanje.framework.database.daos.FavoriteDao
import com.juanje.framework.database.daos.MovieDao
import com.juanje.framework.database.daos.PageDao
import com.juanje.framework.database.daos.UserDao
import com.juanje.framework.database.dataclasses.FavoriteDatabase
import com.juanje.framework.database.dataclasses.MovieDatabase
import com.juanje.framework.database.dataclasses.PageDatabase
import com.juanje.framework.database.dataclasses.UserDatabase

@Database(
    entities = [
        UserDatabase::class,
        MovieDatabase::class,
        PageDatabase::class,
        FavoriteDatabase::class
    ], version = 11)
abstract class TheMoviesAppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun movieDao(): MovieDao
    abstract fun pageDao(): PageDao
    abstract fun favoriteDao(): FavoriteDao
}