package com.juanje.themoviesapp.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.Movie
import com.juanje.themoviesapp.common.InternetAvailable
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadMovie: LoadMovie,
    private val internetAvailable: InternetAvailable,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext val context: Context
) : ViewModel() {

    companion object {
        const val PAGE_THRESHOLD = 10
    }

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val lastVisible = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            lastVisible.collect {
                if (lastVisible.value != 0) {
                    notifyLastVisible(it)
                }
            }
        }
        _state.value = UiState(isInit = true)
    }

    fun getMovies(userName: String) = viewModelScope.launch {
        _state.value = UiState(loading = true)

        val size = loadMovie.invokeGetCountMovies(userName)
        val connectivity = internetAvailable.isInternetAvailable(context)

        loadMovie.invokeGetMovies(userName, lastVisible.value, size, connectivity).collect {
            _state.value = UiState(
                movies = it,
                userName = userName
            )
        }
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch {
        loadMovie.invokeUpdateMovie(movie.copy(favourite = !movie.favourite))
    }

    fun updateLastVisible(lastVisiblePosition: Int) {
        lastVisible.value = lastVisiblePosition
    }

    fun resetState() {
        _state.value = UiState()
    }

    private suspend fun notifyLastVisible(lastVisible: Int) {
        val size = loadMovie.invokeGetCountMovies(_state.value.userName)
        val connectivity = internetAvailable.isInternetAvailable(context)

        if (lastVisible+1 >= size - PAGE_THRESHOLD) {
            loadMovie.invokeGetMovies(_state.value.userName, lastVisible+1, size, connectivity)
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val isInit: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val userName: String = ""
    )
}