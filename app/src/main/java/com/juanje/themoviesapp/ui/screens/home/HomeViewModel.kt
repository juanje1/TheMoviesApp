package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.Movie
import com.juanje.usecases.LoadPopularMovies
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
                if(lastVisible.value != 0) notifyLastVisible(it)
            }
        }
        viewModelScope.launch {
            _state.value = UiState(loading = true)

            val size = loadPopularMovies.invokeGetCountMovies()
            loadPopularMovies.invokeGetMovies(lastVisible.value, size).collect {
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
        if(lastVisible+1 >= size - PAGE_THRESHOLD) {
            loadPopularMovies.invokeGetMovies(lastVisible+1, size)
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList()
    )
}