package com.juanje.themoviesapp.framework.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.themoviesapp.domain.Movie
import com.juanje.themoviesapp.usecases.LoadPopularMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val loadPopularMovies: LoadPopularMovies)
    : ViewModel() {

    companion object {
        const val PAGE_THRESHOLD = 10
    }

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    val lastVisible = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            lastVisible.collect {
                notifyLastVisible(it)
            }
        }
        viewModelScope.launch {
            _state.value = UiState(loading = true)

            loadPopularMovies.invokeGetMovies().collect {
                _state.value = UiState(movies = it)
            }
        }
    }

    fun onMovieClick(movie: Movie) {
        viewModelScope.launch {
            loadPopularMovies.invokeUpdateMovie(movie.copy(favourite = !movie.favourite))
        }
    }

    private suspend fun notifyLastVisible(lastVisible: Int) {
        val size = loadPopularMovies.invokeGetCountMovies()
        if(lastVisible >= size - PAGE_THRESHOLD) {
            loadPopularMovies.invokeGetMovies()
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}