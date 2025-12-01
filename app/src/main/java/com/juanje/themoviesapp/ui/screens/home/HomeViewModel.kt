package com.juanje.themoviesapp.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.Movie
import com.juanje.themoviesapp.common.InternetAvailable
import com.juanje.themoviesapp.data.MainDispatcher
import com.juanje.themoviesapp.utils.EspressoIdlingResource
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadMovie: LoadMovie,
    private val internetAvailable: InternetAvailable,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val _isDataLoading = MutableStateFlow(false)
    private val isDataLoading: StateFlow<Boolean> = _isDataLoading

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading: StateFlow<Boolean> = _isImageLoading

    fun getMovies(userName: String) = viewModelScope.launch(mainDispatcher) {
        if (isDataLoading.value) return@launch

        _state.value = _state.value.copy(loading = true)
        _isDataLoading.value = true
        EspressoIdlingResource.increment()

        try {
            val connectivity = internetAvailable.isInternetAvailable(context)
            _state.value = _state.value.copy(
                loading = false,
                movies = loadMovie.invokeGetMovies(userName, connectivity).first(),
                userName = userName
            )
        } finally {
            _isDataLoading.value = false
            EspressoIdlingResource.decrement()
        }
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch(mainDispatcher) {
        EspressoIdlingResource.increment()

        try {
            loadMovie.invokeUpdateMovie(movie.copy(favourite = !movie.favourite))
        } finally {
            EspressoIdlingResource.decrement()
        }
    }

    fun setIsImageLoading(isImageLoading: Boolean) {
        _isImageLoading.value = isImageLoading
    }

    data class UiState(
        val loading: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val userName: String = ""
    )
}