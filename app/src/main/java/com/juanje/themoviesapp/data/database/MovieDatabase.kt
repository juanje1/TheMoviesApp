package com.juanje.themoviesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.juanje.themoviesapp.data.database.daos.MovieDao
import com.juanje.themoviesapp.data.database.entities.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
}