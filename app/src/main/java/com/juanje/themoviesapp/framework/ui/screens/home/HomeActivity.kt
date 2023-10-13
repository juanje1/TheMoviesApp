package com.juanje.themoviesapp.framework.ui.screens.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.juanje.themoviesapp.data.repositories.MovieRepository
import com.juanje.themoviesapp.framework.data.database.DatabaseDataSource
import com.juanje.themoviesapp.framework.data.database.MovieDatabase
import com.juanje.themoviesapp.framework.data.server.ServerDataSource
import com.juanje.themoviesapp.usecases.LoadPopularMovies

class HomeActivity : ComponentActivity() {

    private lateinit var db : MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java, "movies-db"
        ).build()

        val repository = MovieRepository(
            localDataSource = DatabaseDataSource(db.movieDao()),
            remoteDataSource = ServerDataSource()
        )

        setContent {
            Home(LoadPopularMovies(repository))
        }
    }
}