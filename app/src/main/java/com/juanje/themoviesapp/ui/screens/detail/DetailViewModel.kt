package com.juanje.themoviesapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.Movie
import com.juanje.usecases.LoadMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val loadMovie: LoadMovie) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        _state.value = UiState(isInit = true)
    }

    fun resetInit() { _state.value = UiState() }

    fun getMovieDetail(userName: String, movieId: Int) = viewModelScope.launch {
        _state.value = UiState(movie = loadMovie.invokeGetMovieDetail(userName, movieId))
    }

    data class UiState(
        val movie: Movie ?= null,
        val isInit: Boolean = false
    )
}