package com.juanje.themoviesapp.framework.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.juanje.themoviesapp.framework.ui.theme.TheMoviesAppTheme
import com.juanje.themoviesapp.usecases.LoadPopularMovies

@Composable
fun Home(loadPopularMovies: LoadPopularMovies) {
    TheMoviesAppTheme {

        val viewModel: HomeViewModel = viewModel { HomeViewModel(loadPopularMovies) }
        val state by viewModel.state.collectAsState()

        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = { TopAppBar(title = { Text(text = "Movies") }) }
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
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(120.dp),
                        modifier = Modifier.padding(padding),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(state.movies) { movie ->
                            HomeItem(
                                movie = movie,
                                onClick = { viewModel.onMovieClick(movie) }
                            )
                        }
                    }
                }
            }
        }
    }
}