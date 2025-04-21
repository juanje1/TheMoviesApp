package com.juanje.themoviesapp.ui.screens.detail

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.juanje.themoviesapp.ui.screens.common.ArrowBackIcon

@Composable
fun DetailScreen(
    onHomeClick: (String) -> Unit,
    userName: String,
    movieId: Int
) {
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
                    title = { Text(text = it.title) },
                    navigationIcon = { ArrowBackIcon(onHomeClick, userName) }
                )
            }
        ) { padding ->
            DetailItem(
                padding = padding,
                movie = it
            )
        }
    }
}