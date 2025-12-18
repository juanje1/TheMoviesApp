package com.juanje.themoviesapp.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.Movie
import com.juanje.domain.MovieFavorite
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.ConnectivityObserver
import com.juanje.themoviesapp.data.MainDispatcher
import com.juanje.themoviesapp.utils.EspressoIdlingResource
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val loadMovie: LoadMovie,
    connectivityObserver: ConnectivityObserver,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private val _isImageLoading = MutableStateFlow(false)
    val isImageLoading: StateFlow<Boolean> = _isImageLoading

    private val _userNameFlow = MutableStateFlow<String?>(null)

    init {
        connectivityObserver
            .observe()
            .onEach { isAvailable ->
                _state.update { it.copy(isInternetAvailable = isAvailable) }
            }.launchIn(viewModelScope)

        _userNameFlow.filterNotNull().flatMapLatest { userName ->
            loadMovie.invokeGetMovieFavorites(userName).onEach { movieList ->
                _state.update { it.copy(movies = movieList, userName = userName, isInitialLoading = false) }
            }
        }.catch { e ->
            _state.update { it.copy(error = e.message ?: context.getString(R.string.error_internet), isInitialLoading = false) }
        }.launchIn(viewModelScope)

        _userNameFlow.filterNotNull().onEach { userName ->
            val count = loadMovie.invokeCount(userName)
            if (count == 0) getAndInsertMovies(userName, refresh = true)
        }.launchIn(viewModelScope)
    }

    fun getAndInsertMovies(userName: String, refresh: Boolean = false) = viewModelScope.launch(mainDispatcher) {
        if (_state.value.isGettingMovies || _state.value.isRefreshingMovies) return@launch

        if (!_state.value.isInternetAvailable) {
            _state.update { it.copy(error = context.getString(R.string.error_internet)) }
            return@launch
        }

        if (refresh) _state.update { it.copy(isGettingMovies = false, isRefreshingMovies = true, error = null) }
        else _state.update { it.copy(isGettingMovies = true, isRefreshingMovies = false, error = null) }

        EspressoIdlingResource.increment()

        try {
            loadMovie.invokeGetAndInsertMovies(userName, refresh)
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message ?: context.getString(R.string.error_unknown)) }
        } finally {
            _state.update { it.copy(isGettingMovies = false, isRefreshingMovies = false) }
            EspressoIdlingResource.decrement()
        }
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch(mainDispatcher) {
        val userName = _userNameFlow.value ?: return@launch

        if (_state.value.isUpdatingMovies) return@launch

        _state.value = _state.value.copy(isUpdatingMovies = true, error = null)
        EspressoIdlingResource.increment()

        try {
            val favorite = _state.value.movies.find { it.movie.id == movie.id }?.isFavorite ?: return@launch
            loadMovie.invokeUpdateMovie(userName, movie, !favorite)
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message ?: context.getString(R.string.error_unknown)) }
        } finally {
            _state.update { it.copy(isUpdatingMovies = false) }
            EspressoIdlingResource.decrement()
        }
    }

    fun setIsImageLoading(isImageLoading: Boolean) {
        _isImageLoading.value = isImageLoading
    }

    fun setUserNameFlow(userName: String) {
        if (_userNameFlow.value != userName) {
            _userNameFlow.value = userName
        }
    }

    data class UiState(
        val movies: List<MovieFavorite> = emptyList(),
        val userName: String = "",
        val isInternetAvailable: Boolean = true,
        val isInitialLoading: Boolean = true,
        val isGettingMovies: Boolean = false,
        val isUpdatingMovies: Boolean = false,
        val isRefreshingMovies: Boolean = false,
        val error: String? = null
    )
}