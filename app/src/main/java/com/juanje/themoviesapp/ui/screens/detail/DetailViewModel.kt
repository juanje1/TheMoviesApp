package com.juanje.themoviesapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.MainDispatcher
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.themoviesapp.common.AppIdlingResource
import com.juanje.themoviesapp.common.toErrorRes
import com.juanje.themoviesapp.common.trackFlow
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val loadMovie: LoadMovie,
    private val idlingResource: AppIdlingResource,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private data class DetailArgs(val userName: String, val movieId: Int)
    private val _argsFlow = MutableStateFlow<DetailArgs?>(null)

    private fun detailHandler(onCleanup: () -> Unit = {}) = CoroutineExceptionHandler { _, e ->
        _state.update { it.copy(error = e.toErrorRes()) }
        onCleanup()
    }

    init {
        observeMovieFavorite()
    }

    private fun observeMovieFavorite() = viewModelScope.launch(mainDispatcher + detailHandler()) {
        val args = _argsFlow.filterNotNull().first()

        loadMovie.invokeGetMovieFavorite(args.userName, args.movieId)
            .trackFlow(idlingResource)
            .onEach { movieFavorite -> _state.update { it.copy(movie = movieFavorite) } }
            .catch { e -> _state.update { it.copy(error = e.toErrorRes()) } }
            .launchIn(viewModelScope)
    }

    fun setArgsFlow(userName: String, movieId: Int) {
        _argsFlow.value = DetailArgs(userName, movieId)
    }

    fun resetError() {
        _state.update { it.copy(error = null) }
    }

    data class UiState(
        val movie: MovieFavorite?= null,
        val error: Int? = null
    )
}