package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.juanje.domain.MainDispatcher
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.themoviesapp.common.AppIdlingResource
import com.juanje.themoviesapp.common.ConnectivityObserver
import com.juanje.themoviesapp.common.createHandler
import com.juanje.themoviesapp.common.trackFlow
import com.juanje.themoviesapp.common.trackLoading
import com.juanje.themoviesapp.ui.navigation.Screen
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import java.util.Collections.emptyList
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadMovie: LoadMovie,
    private val idlingResource: AppIdlingResource,
    private val connectivityObserver: ConnectivityObserver,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val homeArgs = savedStateHandle.toRoute<Screen.Home>()

    private val _state = MutableStateFlow(UiState(userName = homeArgs.userName))
    val state: StateFlow<UiState> = _state

    private val _userNameFlow = savedStateHandle.getStateFlow<String?>(
        key = Screen.Home::userName.name,
        initialValue = homeArgs.userName
    )

    private fun homeHandler(onCleanup: () -> Unit = {}) = createHandler(
        onUpdateError = { errorRes -> _state.update { it.copy(error = errorRes) } },
        onCleanup = onCleanup
    )

    init {
        _state.update {
            it.copy(isInternetAvailable = connectivityObserver.isConnected())
        }

        observeConnectivity()
        observeMovieFavorites()
        syncMoviesIfNecessary()
    }

    private fun observeConnectivity() {
        connectivityObserver
            .observe()
            .onEach { isAvailable -> _state.update { it.copy(isInternetAvailable = isAvailable) } }
            .launchIn(viewModelScope)
    }

    private fun observeMovieFavorites() = viewModelScope.launch(mainDispatcher + homeHandler {
        _state.update { it.copy(isInitialLoading = false) }
    }) {
       _userNameFlow
           .filterNotNull()
           .flatMapLatest { userName ->
               loadMovie.invokeGetMovieFavorites(userName)
                   .trackFlow(idlingResource) { _state.update { it.copy(isInitialLoading = false) } }
           }.onEach { movieList -> _state.update { it.copy(movies = movieList) } }
           .collect()
    }

    private fun syncMoviesIfNecessary() {
        _userNameFlow
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { userName ->
                trackLoading(idlingResource = idlingResource) {
                    if (loadMovie.invokeCount(userName) == 0) {
                        loadMovie.invokeGetAndInsertMovies(userName, refresh = true)
                    }
                }
            }.launchIn(viewModelScope.plus(homeHandler { _state.update { it.copy(isInitialLoading = false) } }))
    }

    fun getAndInsertMovies(userName: String, refresh: Boolean = false) = viewModelScope.launch(mainDispatcher + homeHandler {
        _state.update { it.copy(isGettingMovies = false, isRefreshingMovies = false) }
    }) {
        if (_state.value.isGettingMovies || _state.value.isRefreshingMovies) return@launch

        if (refresh) _state.update { it.copy(isGettingMovies = false, isRefreshingMovies = true, error = null) }
        else _state.update { it.copy(isGettingMovies = true, isRefreshingMovies = false, error = null) }

        trackLoading(idlingResource = idlingResource) {
            loadMovie.invokeGetAndInsertMovies(userName, refresh)
        }
        _state.update { it.copy(isGettingMovies = false, isRefreshingMovies = false) }
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch(mainDispatcher + homeHandler {
        _state.update { it.copy(isUpdatingMovies = false) }
    }) {
        val userName = _userNameFlow.value ?: return@launch
        if (_state.value.isUpdatingMovies) return@launch

        val favorite = _state.value.movies.find { it.movie.id == movie.id }?.isFavorite ?: return@launch

        _state.update { it.copy(isUpdatingMovies = true, error = null) }

        trackLoading(idlingResource = idlingResource) {
            loadMovie.invokeUpdateMovieFavorite(userName, movie, !favorite)
        }
        _state.update { it.copy(isUpdatingMovies = false) }
    }

    fun resetError() {
        _state.update { it.copy(error = null) }
    }

    data class UiState(
        val movies: List<MovieFavorite> = emptyList(),
        val userName: String = "",
        val isInternetAvailable: Boolean = false,
        val isInitialLoading: Boolean = true,
        val isGettingMovies: Boolean = false,
        val isUpdatingMovies: Boolean = false,
        val isRefreshingMovies: Boolean = false,
        val error: Int? = null,
    )
}