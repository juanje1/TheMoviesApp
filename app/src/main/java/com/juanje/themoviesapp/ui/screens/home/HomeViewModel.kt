package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.themoviesapp.data.Movie
import com.juanje.themoviesapp.data.local.MoviesDao
import com.juanje.themoviesapp.data.local.toLocalMovie
import com.juanje.themoviesapp.data.local.toMovie
import com.juanje.themoviesapp.data.remote.MoviesService
import com.juanje.themoviesapp.data.remote.toLocalMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel(private val dao: MoviesDao) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        viewModelScope.launch {
            val isDbEmpty = dao.count() == 0
            if (isDbEmpty) {
                _state.value = UiState(loading = true)
                dao.insertAll(
                    Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(MoviesService::class.java)
                        .getMovies()
                        .results
                        .map { it.toLocalMovie() }
                )
            }

            dao.getMovies().collect { movies ->
                _state.value = UiState(
                    movies = movies.map { it.toMovie() }
                )
            }
        }
    }

    fun onMovieClick(movie: Movie) {
        viewModelScope.launch {
            dao.updateMovie(movie.copy(favourite = !movie.favourite).toLocalMovie())
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}