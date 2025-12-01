package com.juanje.themoviesapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.Movie
import com.juanje.themoviesapp.data.MainDispatcher
import com.juanje.themoviesapp.utils.EspressoIdlingResource
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val loadMovie: LoadMovie,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading: StateFlow<Boolean> = _isImageLoading

    fun getMovieDetail(userName: String, movieId: Int) = viewModelScope.launch(mainDispatcher) {
        EspressoIdlingResource.increment()

        try {
            _state.value = UiState(movie = loadMovie.invokeGetMovieDetail(userName, movieId))
        } finally {
            EspressoIdlingResource.decrement()
        }
    }

    fun setIsImageLoading(isImageLoading: Boolean) {
        _isImageLoading.value = isImageLoading
    }

    data class UiState(
        val movie: Movie ?= null
    )
}