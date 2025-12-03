package com.juanje.themoviesapp.ui.screens.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.Movie
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

        _userNameFlow
            .filterNotNull()
            .flatMapLatest { userName ->
                loadMovie.invokeGetMoviesFromDatabase(userName)
                    .onEach { movieList ->
                        if (movieList.isEmpty() && !_state.value.isGettingMovies)
                            getMovies(userName)
                    }.map { movieList -> userName to movieList }
            }.onEach { (userName, movieList) ->
                val uniqueMoviesList = filterDuplicatesInList(movieList)
                _state.update { it.copy(movies = uniqueMoviesList, userName = userName, isInitialLoading = false) }
            }.catch { e ->
                _state.update { it.copy(error = e.message ?: context.getString(R.string.error_internet), isInitialLoading = false) }
            }.launchIn(viewModelScope)
    }

    fun getMovies(userName: String) = viewModelScope.launch(mainDispatcher) {
        if (_state.value.isGettingMovies) return@launch

        if (!_state.value.isInternetAvailable) {
            _state.update { it.copy(error = context.getString(R.string.error_internet)) }
            return@launch
        }

        _state.update { it.copy(isGettingMovies = true, error = null) }
        EspressoIdlingResource.increment()

        try {
            loadMovie.invokeGetMovies(userName)
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message ?: context.getString(R.string.error_unknown)) }
        } finally {
            _state.update { it.copy(isGettingMovies = false) }
            EspressoIdlingResource.decrement()
        }
    }

    fun updateMovie(movie: Movie) = viewModelScope.launch(mainDispatcher) {
        if (_state.value.isUpdatingMovies) return@launch

        _state.value = _state.value.copy(isUpdatingMovies = true)
        EspressoIdlingResource.increment()

        try {
            loadMovie.invokeUpdateMovie(movie.copy(favourite = !movie.favourite))
        } catch (e: Exception) {
            _state.update { it.copy(error = e.message ?: context.getString(R.string.error_unknown)) }
        } finally {
            _state.update { it.copy(isUpdatingMovies = false) }
            EspressoIdlingResource.decrement()
        }
    }

    private fun filterDuplicatesInList(list: List<Movie>): List<Movie> {
        val seenKeys = mutableSetOf<String>()
        return list.filter { item ->
            val key = "${item.title}|${item.releaseDate}"
            seenKeys.add(key)
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
        val movies: List<Movie> = emptyList(),
        val userName: String = "",
        val isInternetAvailable: Boolean = true,
        val isInitialLoading: Boolean = true,
        val isGettingMovies: Boolean = false,
        val isUpdatingMovies: Boolean = false,
        val error: String? = null
    )
}