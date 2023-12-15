package com.juanje.themoviesapp.ui.screens.detail

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DetailScreen(userName: String, movieId: Int) {
    val viewModel: DetailViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    if (state.isInit) {
        viewModel.resetInit()
        viewModel.getMovieDetail(userName, movieId)
    }

    state.movie?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = it.title)
                    }
                )
            }
        ) { padding ->
            DetailItem(padding, it)
        }
    }
}