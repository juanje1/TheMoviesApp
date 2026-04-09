package com.juanje.themoviesapp.ui.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.juanje.data.common.toAppError
import com.juanje.domain.MainDispatcher
import com.juanje.domain.dataclasses.Movie
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.themoviesapp.common.enums.MovieCategory
import com.juanje.themoviesapp.common.extensions.createHandler
import com.juanje.themoviesapp.common.extensions.trackFlow
import com.juanje.themoviesapp.common.extensions.trackLoading
import com.juanje.themoviesapp.common.network.ConnectivityObserver
import com.juanje.themoviesapp.common.utils.AppIdlingResource
import com.juanje.themoviesapp.ui.navigation.Screen
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private val _userNameFlow = savedStateHandle.getStateFlow<String?>(
        key = Screen.Home::userName.name,
        initialValue = homeArgs.userName
    )
    private val _categoryFlow = MutableStateFlow(MovieCategory.POPULAR)

    private val _state = MutableStateFlow(UiState(userName = homeArgs.userName))
    val state: StateFlow<UiState> = _state

    val movies: Flow<PagingData<MovieFavorite>> = combine(
        _userNameFlow.filterNotNull(),
        _categoryFlow
    ) { userName, category ->
        _state.update { it.copy(userName = userName, category = category) }
        loadMovie.invokeGetMovies(userName, category.queryValue)
            .trackFlow(idlingResource) { _state.update { it.copy(isInitialLoading = false) } }
    }.flatMapLatest { it }
    .cachedIn(viewModelScope)

    private fun homeHandler(onCleanup: () -> Unit = {}) = createHandler(
        onUpdateError = { errorRes -> _state.update { it.copy(error = errorRes) } },
        onCleanup = onCleanup
    )

    init {
        _state.update { it.copy(isInternetAvailable = connectivityObserver.isConnected()) }

        connectivityObserver.observe()
            .onEach { isAvailable -> _state.update { it.copy(isInternetAvailable = isAvailable) } }
            .launchIn(viewModelScope)
    }

    fun updateMovie(movie: Movie, favorite: Boolean) = viewModelScope.launch(mainDispatcher + homeHandler {
        _state.update { it.copy(isUpdatingMovies = false) }
    }) {
        if (_state.value.isUpdatingMovies) return@launch

        _state.update { it.copy(isUpdatingMovies = true, error = null) }

        trackLoading(idlingResource = idlingResource) {
            loadMovie.invokeUpdateMovie(movie, favorite)
        }
        _state.update { it.copy(isUpdatingMovies = false) }
    }

    fun resetError() {
        _state.update { it.copy(error = null) }
    }

    fun handlePagingError(throwable: Throwable) {
        viewModelScope.launch(mainDispatcher + homeHandler {
            _state.update { it.copy(isInitialLoading = false) }
        }) {
            throw throwable.toAppError()
        }
    }

    data class UiState(
        val userName: String = "",
        val category: MovieCategory = MovieCategory.POPULAR,
        val isInternetAvailable: Boolean = false,
        val isInitialLoading: Boolean = true,
        val isUpdatingMovies: Boolean = false,
        val error: Int? = null,
    )
}