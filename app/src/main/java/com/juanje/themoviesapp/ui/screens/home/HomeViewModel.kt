package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.themoviesapp.data.Movie
import com.juanje.themoviesapp.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        viewModelScope.launch {
            _state.value = UiState(loading = true)
            repository.requestMovies()

            repository.movies.collect {
                _state.value = UiState(movies = it)
            }
        }
    }

    fun onMovieClick(movie: Movie) {
        viewModelScope.launch {
            repository.updateMovie(movie.copy(favourite = !movie.favourite))
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}