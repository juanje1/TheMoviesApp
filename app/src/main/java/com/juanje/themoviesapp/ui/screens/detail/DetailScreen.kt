package com.juanje.themoviesapp.ui.screens.detail

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.juanje.themoviesapp.ui.screens.common.ArrowBackIcon

@Composable
fun DetailScreen(
    onClickBack: () -> Unit,
    userName: String,
    movieId: Int
) {
    val viewModel: DetailViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    if (state.isInit) {
        viewModel.resetInit()
        viewModel.getMovieDetail(userName, movieId)
    }

    state.movie?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = it.title) },
                    navigationIcon = { ArrowBackIcon(onClickBack, context) }
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