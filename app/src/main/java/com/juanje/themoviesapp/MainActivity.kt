package com.juanje.themoviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.juanje.themoviesapp.data.MoviesRepository
import com.juanje.themoviesapp.data.local.LocalDataSource
import com.juanje.themoviesapp.data.local.MoviesDatabase
import com.juanje.themoviesapp.data.remote.RemoteDataSource
import com.juanje.themoviesapp.ui.screens.home.Home

class MainActivity : ComponentActivity() {

    private lateinit var db : MoviesDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java, "movies-db"
        ).build()

        val repository = MoviesRepository(
            localDataSource = LocalDataSource(db.moviesDao()),
            remoteDataSource = RemoteDataSource()
        )

        setContent {
            Home(repository)
        }
    }
}