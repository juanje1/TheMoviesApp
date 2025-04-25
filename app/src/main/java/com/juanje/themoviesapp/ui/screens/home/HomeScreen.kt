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
import com.juanje.themoviesapp.ui.screens.common.dialogs.LogoutAlertDialog
import com.juanje.themoviesapp.ui.screens.common.others.MyTopAppBar

@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onDetailClick: (String, Int) -> Unit,
    userName: String
) {
    var showLogoutAlertDialog by rememberSaveable { mutableStateOf(false) }

    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeState by homeViewModel.state.collectAsState()

    if (homeState.isInit) {
        homeViewModel.resetState()
        homeViewModel.getMovies(userName)
    }

    if (showLogoutAlertDialog) {
        LogoutAlertDialog(
            onAccept = { onLoginClick() },
            onCancel = { showLogoutAlertDialog = false },
            onDismiss = { showLogoutAlertDialog = false }
        )
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = dimensionResource(R.dimen.shadow_elevation_topBar)) {
                MyTopAppBar(
                    userName = userName,
                    onLogoutClick = { showLogoutAlertDialog = true }
                )
            }
        }
    ) { padding ->
        if(homeState.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        if (homeState.movies.isNotEmpty()) {
            val listState = rememberLazyGridState()

            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Adaptive(dimensionResource(R.dimen.column_min_width)),
                modifier = Modifier.padding(padding),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                contentPadding = PaddingValues(dimensionResource(R.dimen.padding_xsmall))
            ) {
                items(homeState.movies) { movie ->
                    HomeItem(
                        onDetailClick = onDetailClick,
                        onFavouriteClick = { homeViewModel.updateMovie(movie) },
                        movie = movie
                    )
                    val lastVisiblePosition = listState.isScrolledToTheEnd()

                    if (lastVisiblePosition != null) homeViewModel.setLastVisible(lastVisiblePosition)
                    else homeViewModel.setLastVisible(0)
                }
            }
        }
    }
}

fun LazyGridState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index