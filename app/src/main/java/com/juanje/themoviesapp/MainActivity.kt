package com.juanje.themoviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.juanje.themoviesapp.data.MovieRepository
import com.juanje.themoviesapp.data.database.DatabaseDataSource
import com.juanje.themoviesapp.data.database.MovieDatabase
import com.juanje.themoviesapp.data.server.ServerDataSource
import com.juanje.themoviesapp.ui.screens.home.Home

class MainActivity : ComponentActivity() {

    private lateinit var db : MovieDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java, "movies-db"
        ).build()

        val repository = MovieRepository(
            databaseDataSource = DatabaseDataSource(db.movieDao()),
            serverDataSource = ServerDataSource()
        )

        setContent {
            Home(repository)
        }
    }
}