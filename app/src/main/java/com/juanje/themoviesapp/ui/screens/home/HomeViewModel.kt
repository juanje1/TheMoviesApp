package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.themoviesapp.data.remote.MovieService
import com.juanje.themoviesapp.data.remote.ServerMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            _state.value = UiState(
                movies = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService::class.java)
                .getMovies()
                .results
            )
        }
    }

    fun onMovieClick(movie: ServerMovie) {
        val movies = _state.value.movies.toMutableList()
        movies.replaceAll {
            if (it.id == movie.id) movie.copy(favourite = !it.favourite) else it
        }
        _state.value = _state.value.copy(movies = movies)
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<ServerMovie> = emptyList()
    )
}