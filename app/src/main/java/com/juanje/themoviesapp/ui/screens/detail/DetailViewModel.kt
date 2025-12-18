package com.juanje.themoviesapp.ui.screens.detail

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.MovieFavorite
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.utils.EspressoIdlingResource
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading: StateFlow<Boolean> = _isImageLoading

    private data class DetailArgs(val userName: String, val movieId: Int)
    private val _argsFlow = MutableStateFlow<DetailArgs?>(null)

    init {
        _argsFlow
            .onEach { EspressoIdlingResource.increment() }
            .filterNotNull()
            .flatMapLatest { args -> loadMovie.invokeGetMovieFavorite(args.userName, args.movieId) }
            .onEach { movieFavorite ->
                _state.update { it.copy(movieFavorite = movieFavorite) }
                EspressoIdlingResource.decrement()
            }.catch { e ->
                _state.update { it.copy(error = e.message ?: context.getString(R.string.error_internet)) }
                EspressoIdlingResource.decrement()
            }.launchIn(viewModelScope)
    }

    fun setIsImageLoading(isImageLoading: Boolean) {
        _isImageLoading.value = isImageLoading
    }

    fun setArgsFlow(userName: String, movieId: Int) {
        _argsFlow.value = DetailArgs(userName, movieId)
    }

    data class UiState(
        val movieFavorite: MovieFavorite ?= null,
        val error: String? = null
    )
}