package com.juanje.themoviesapp.ui.screens.detail

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DetailScreen(userName: String, movieId: Int) {
    val viewModel: DetailViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    if (state.isInit) {
        viewModel.resetInit()
        viewModel.getMoviesDetail(userName)
    }

    if (state.movies.isNotEmpty()) {
        val movie = remember { state.movies.first { it.id == movieId } }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = movie.title)
                    }
                )
            }
        ) { padding ->
            DetailItem(padding, movie)
        }
    }
}