package com.juanje.themoviesapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.juanje.themoviesapp.data.database.dataclasses.MovieDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieDatabase WHERE userName = :userName")
    fun getMovies(userName: String): Flow<List<MovieDatabase>>

    @Query("SELECT * FROM MovieDatabase WHERE userName = :userName AND id = :movieId")
    suspend fun getMovie(userName: String, movieId: Int): MovieDatabase

    @Query("SELECT COUNT(*) FROM MovieDatabase WHERE userName = :userName")
    suspend fun count(userName: String): Int

    @Insert
    suspend fun insertAll(movies: List<MovieDatabase>)

    @Update
    suspend fun updateMovie(movie: MovieDatabase)
}