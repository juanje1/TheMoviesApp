package com.juanje.themoviesapp.data.database.daos

import androidx.room.*
import com.juanje.themoviesapp.data.database.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieEntity")
    fun getMovies(): Flow<List<MovieEntity>>

    @Insert
    suspend fun insertAll(movies: List<MovieEntity>)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Query("SELECT COUNT(*) FROM MovieEntity")
    suspend fun count(): Int
}