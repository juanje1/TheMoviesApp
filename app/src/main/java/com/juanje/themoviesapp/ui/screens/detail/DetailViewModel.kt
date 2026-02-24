package com.juanje.themoviesapp.ui.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juanje.domain.MainDispatcher
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.themoviesapp.common.AppIdlingResource
import com.juanje.themoviesapp.common.createHandler
import com.juanje.themoviesapp.common.trackFlow
import com.juanje.themoviesapp.ui.navigation.Screen
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val loadMovie: LoadMovie,
    private val idlingResource: AppIdlingResource,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val detailArgs = savedStateHandle.toRoute<Screen.Detail>()

    private val _state = MutableStateFlow(UiState(userName = detailArgs.userName))
    val state: StateFlow<UiState> = _state

    private fun detailHandler(onCleanup: () -> Unit = {}) = createHandler(
        onUpdateError = { errorRes -> _state.update { it.copy(error = errorRes) } },
        onCleanup = onCleanup
    )

    init {
        observeMovieFavorite()
    }

    private fun observeMovieFavorite() {
        loadMovie.invokeGetMovieFavorite(detailArgs.userName, detailArgs.movieId)
            .trackFlow(idlingResource)
            .onEach { movieFavorite -> _state.update { it.copy(movie = movieFavorite) } }
            .launchIn(viewModelScope.plus(mainDispatcher + detailHandler()))
    }

    fun resetError() {
        _state.update { it.copy(error = null) }
    }

    data class UiState(
        val userName: String = "",
        val movie: MovieFavorite?= null,
        val error: Int? = null
    )
}