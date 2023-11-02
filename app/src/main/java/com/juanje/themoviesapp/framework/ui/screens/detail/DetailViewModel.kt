package com.juanje.themoviesapp.framework.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.themoviesapp.domain.Movie
import com.juanje.themoviesapp.usecases.LoadPopularMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val loadPopularMovies: LoadPopularMovies)
    : ViewModel() {

    private val _state = MutableStateFlow<List<Movie>>(emptyList())
    val state: StateFlow<List<Movie>> = _state

    init {
        viewModelScope.launch {
            loadPopularMovies.invokeGetMoviesDetail().collect { _state.value = it }
        }
    }
}