package com.juanje.themoviesapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.MovieFavorite
import com.juanje.themoviesapp.common.AppIdlingResource
import com.juanje.themoviesapp.common.toErrorRes
import com.juanje.themoviesapp.common.trackLoading
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val loadMovie: LoadMovie,
    private val idlingResource: AppIdlingResource
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading: StateFlow<Boolean> = _isImageLoading

    private data class DetailArgs(val userName: String, val movieId: Int)
    private val _argsFlow = MutableStateFlow<DetailArgs?>(null)

    init {
        observeMovieFavorite()
    }

    private fun observeMovieFavorite() {
        _argsFlow
            .filterNotNull()
            .flatMapLatest { args ->
                loadMovie.invokeGetMovieFavorite(args.userName, args.movieId)
                    .trackLoading(idlingResource)
            }.onEach { movieFavorite ->
                _state.update { it.copy(movie = movieFavorite) }
            }.catch { e ->
                _state.update { it.copy(error = e.toErrorRes()) }
            }.launchIn(viewModelScope)
    }

    fun setIsImageLoading(isImageLoading: Boolean) {
        _isImageLoading.value = isImageLoading
    }

    fun setArgsFlow(userName: String, movieId: Int) {
        _argsFlow.value = DetailArgs(userName, movieId)
    }

    fun resetError() {
        _state.update { it.copy(error = null) }
    }

    data class UiState(
        val movie: MovieFavorite ?= null,
        val error: Int? = null
    )
}