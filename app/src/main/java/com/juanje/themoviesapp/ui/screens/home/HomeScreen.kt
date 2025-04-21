package com.juanje.themoviesapp.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.juanje.themoviesapp.R

@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onDetailClick: (String, Int) -> Unit,
    userName: String
) {
    var showLogoutAlertDialog by rememberSaveable { mutableStateOf(false) }

    val homeViewModel: HomeViewModel = hiltViewModel()
    val state by homeViewModel.state.collectAsState()

    if (state.isInit) {
        homeViewModel.resetState()
        homeViewModel.getMovies(userName)
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = dimensionResource(R.dimen.shadow_elevation_topBar)) {
                /**MyTopAppBar(
                    user = userName,
                    onLogoutClick = { showLogoutAlertDialog = true }
                )**/
            }
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
        //if (state.movies.isNotEmpty()) {
            val listState = rememberLazyGridState()

            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Adaptive(dimensionResource(R.dimen.column_min_width)),
                modifier = Modifier.padding(padding),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                contentPadding = PaddingValues(dimensionResource(R.dimen.padding_xsmall))
            ) {
                items(state.movies) { movie ->
                    HomeItem(
                        onDetailClick = onDetailClick,
                        onFavouriteClick = { homeViewModel.onMovieClick(movie) },
                        movie = movie
                    )
                    val lastVisiblePosition = listState.isScrolledToTheEnd()

                    if (lastVisiblePosition != null) homeViewModel.lastVisible.value = lastVisiblePosition
                    else homeViewModel.lastVisible.value = 0
                }
            }
        //}
    }
}

fun LazyGridState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index