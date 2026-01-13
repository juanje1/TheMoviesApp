package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.Movie
import com.juanje.domain.MovieFavorite
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.AppIdlingResource
import com.juanje.themoviesapp.common.ConnectivityObserver
import com.juanje.themoviesapp.common.toErrorRes
import com.juanje.themoviesapp.common.trackLoading
import com.juanje.themoviesapp.data.MainDispatcher
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadMovie: LoadMovie,
    private val idlingResource: AppIdlingResource,
    private val connectivityObserver: ConnectivityObserver,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading: StateFlow<Boolean> = _isImageLoading

    private val _userNameFlow = MutableStateFlow<String?>(null)

    init {
        observeConnectivity()
        observeMovieFavorites()
        syncMoviesIfNecessary()
    }

    private fun observeConnectivity() {
        connectivityObserver
            .observe()
            .onEach { isAvailable ->
                _state.update { it.copy(isInternetAvailable = isAvailable) }
            }.launchIn(viewModelScope)
    }

    private fun observeMovieFavorites() {
        _userNameFlow
            .filterNotNull()
            .flatMapLatest { userName ->
                loadMovie.invokeGetMovieFavorites(userName)
                    .trackLoading(idlingResource)
                    .map { movieList -> userName to movieList }
            }.onEach { (userName, movieList) ->
                _state.update { it.copy(movies = movieList, userName = userName, isInitialLoading = false) }
            }.catch { e ->
                _state.update { it.copy(error = e.toErrorRes(), isInitialLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun syncMoviesIfNecessary() {
        _userNameFlow
            .filterNotNull()
            .onEach { userName ->
                trackLoading(
                    idlingResource = idlingResource,
                    onError = { e -> _state.update { it.copy(error = e.toErrorRes()) } }
                ) {
                    if (loadMovie.invokeCount(userName) == 0)
                        loadMovie.invokeGetAndInsertMovies(userName, refresh = true)
                }
            }.launchIn(viewModelScope)
    }

    fun getAndInsertMovies(userName: String, refresh: Boolean = false) = viewModelScope.launch(mainDispatcher) {
        if (_state.value.isGettingMovies || _state.value.isRefreshingMovies) return@launch

        if (!_state.value.isInternetAvailable) {
            _state.update { it.copy(error = R.string.error_internet) }
            return@launch
        }

        if (refresh) _state.update { it.copy(isGettingMovies = false, isRefreshingMovies = true, error = null) }
        else _state.update { it.copy(isGettingMovies = true, isRefreshingMovies = false, error = null) }

        trackLoading(
            idlingResource = idlingResource,
            onError = { e -> _state.update { it.copy(error = e.toErrorRes()) } },
            onFinally = { _state.update { it.copy(isGettingMovies = false, isRefreshingMovies = false) } }
        ) {
            loadMovie.invokeGetAndInsertMovies(userName, refresh)
        }
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch(mainDispatcher) {
        val userName = _userNameFlow.value ?: return@launch
        if (_state.value.isUpdatingMovies) return@launch

        val favorite = _state.value.movies.find { it.movie.id == movie.id }?.isFavorite ?: return@launch

        _state.update { it.copy(isUpdatingMovies = true, error = null) }

        trackLoading(
            idlingResource = idlingResource,
            onError = { e -> _state.update { it.copy(error = e.toErrorRes()) } },
            onFinally = { _state.update { it.copy(isUpdatingMovies = false) } }
        ) {
            loadMovie.invokeUpdateMovieFavorite(userName, movie, !favorite)
        }
    }

    fun setIsImageLoading(isImageLoading: Boolean) {
        _isImageLoading.value = isImageLoading
    }

    fun setUserNameFlow(userName: String) {
        _userNameFlow.value = userName
    }

    fun resetError() {
        _state.update { it.copy(error = null) }
    }

    data class UiState(
        val movies: List<MovieFavorite> = emptyList(),
        val userName: String = "",
        val isInternetAvailable: Boolean = true,
        val isInitialLoading: Boolean = true,
        val isGettingMovies: Boolean = false,
        val isUpdatingMovies: Boolean = false,
        val isRefreshingMovies: Boolean = false,
        val error: Int? = null,
    )
}