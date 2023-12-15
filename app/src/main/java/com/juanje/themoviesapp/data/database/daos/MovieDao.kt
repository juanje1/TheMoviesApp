package com.juanje.themoviesapp.data.database.daos

import androidx.room.*
import com.juanje.themoviesapp.data.database.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieEntity WHERE userName = :userName")
    fun getMovies(userName: String): Flow<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity WHERE userName = :userName AND id = :movieId")
    suspend fun getMovie(userName: String, movieId: Int): MovieEntity

    @Query("SELECT COUNT(*) FROM MovieEntity WHERE userName = :userName")
    suspend fun count(userName: String): Int

    @Insert
    suspend fun insertAll(movies: List<MovieEntity>)

    @Update
    suspend fun updateMovie(movie: MovieEntity)
}