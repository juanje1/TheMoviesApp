package com.juanje.themoviesapp.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.juanje.themoviesapp.R

@Composable
fun HomeScreen(navController: NavHostController, userName: String) {
    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    if (state.isInit) {
        viewModel.resetInit()
        viewModel.getMovies(userName)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = context.getString(R.string.movies_title)+" @${userName}")
                }
            )
        }
    ) { padding ->
        if(state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        if(state.movies.isNotEmpty()) {
            val listState = rememberLazyGridState()

            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Adaptive(
                    dimensionResource(R.dimen.column_min_width)),
                modifier = Modifier.padding(padding),
                horizontalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_xsmall)),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(R.dimen.padding_xsmall)),
                contentPadding = PaddingValues(
                    dimensionResource(R.dimen.padding_xsmall))
            ) {
                items(state.movies) { movie ->
                    HomeItem(
                        movie = movie,
                        navController = navController,
                        onClick = { viewModel.onMovieClick(movie) }
                    )
                    val lastVisiblePosition = listState.isScrolledToTheEnd()

                    if(lastVisiblePosition != null)
                        viewModel.lastVisible.value = lastVisiblePosition
                    else
                        viewModel.lastVisible.value = 0
                }
            }
        }
    }
}

fun LazyGridState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index