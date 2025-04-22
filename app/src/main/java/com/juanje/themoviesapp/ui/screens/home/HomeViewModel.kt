package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.Movie
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val loadMovie: LoadMovie) : ViewModel() {

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
        _state.value = UiState(isInit = true)
    }

    fun resetInit() { _state.value = UiState() }

    fun getMovies(userName: String) {
        viewModelScope.launch {
            _state.value = UiState(loading = true)

            val size = loadMovie.invokeGetCountMovies(userName)
            loadMovie.invokeGetMovies(userName, lastVisible.value, size).collect {
                _state.value = UiState(
                    movies = it,
                    userName = userName
                )
            }
        }
    }

    fun onMovieClick(movie: Movie) {
        viewModelScope.launch {
            loadMovie.invokeUpdateMovie(movie.copy(favourite = !movie.favourite))
        }
    }

    private suspend fun notifyLastVisible(lastVisible: Int) {
        val size = loadMovie.invokeGetCountMovies(_state.value.userName)
        if(lastVisible+1 >= size - PAGE_THRESHOLD) {
            loadMovie.invokeGetMovies(_state.value.userName, lastVisible+1, size)
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val isInit: Boolean = false,
        val userName: String = ""
    )
}