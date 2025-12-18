package com.juanje.themoviesapp.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.juanje.themoviesapp.data.database.dataclasses.MovieDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM MovieDatabase WHERE userName = :userName ORDER BY displayOrder ASC")
    fun getMovies(userName: String): Flow<List<MovieDatabase>>

    @Query("SELECT * FROM MovieDatabase WHERE userName = :userName AND id = :movieId")
    fun getMovie(userName: String, movieId: Int): Flow<MovieDatabase>

    @Query("SELECT COUNT(*) FROM MovieDatabase WHERE userName = :userName")
    suspend fun count(userName: String): Int

    @Query("DELETE FROM MovieDatabase WHERE userName = :userName")
    suspend fun deleteAll(userName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieDatabase>)

    @Transaction
    suspend fun refreshMoviesTx(userName: String, movies: List<MovieDatabase>) {
        deleteAll(userName)
        insertAll(movies)
    }
}