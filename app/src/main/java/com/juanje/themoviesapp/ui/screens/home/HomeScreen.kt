package com.juanje.themoviesapp.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.PAGE_THRESHOLD
import com.juanje.themoviesapp.ui.screens.common.dialogs.LogoutAlertDialog
import com.juanje.themoviesapp.ui.screens.common.others.MyTopAppBar
import com.juanje.themoviesapp.utils.EspressoIdlingResource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@OptIn(FlowPreview::class)
@Composable
fun HomeScreen(
    onLogin: () -> Unit,
    onDetail: (String, Int) -> Unit,
    userName: String
) {
    var showLogoutAlertDialog by rememberSaveable { mutableStateOf(false) }

    val homeViewModel: HomeViewModel = hiltViewModel()
    val homeState by homeViewModel.state.collectAsState()

    val listState = rememberLazyGridState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        homeViewModel.getMovies(userName)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .debounce(300L)
            .collect { index ->
                val isIdle = EspressoIdlingResource.countingIdlingResource.isIdleNow

                if (index >= listState.layoutInfo.totalItemsCount - PAGE_THRESHOLD && isIdle) {
                    homeViewModel.getMovies(userName)
                }
            }
    }

    if (showLogoutAlertDialog) {
        LogoutAlertDialog(
            onAccept = { onLogin() },
            onCancel = { showLogoutAlertDialog = false },
            onDismiss = { showLogoutAlertDialog = false }
        )
    }

    Scaffold(
        topBar = {
            Surface(shadowElevation = dimensionResource(R.dimen.shadow_elevation_topBar)) {
                MyTopAppBar(
                    userName = userName,
                    origin = context.getString(R.string.origin_from_home),
                    onLogout = { showLogoutAlertDialog = true }
                )
            }
        }
    ) { padding ->
        if (homeState.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        if (homeState.movies.isNotEmpty()) {
            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Adaptive(dimensionResource(R.dimen.column_min_width)),
                modifier = Modifier
                    .padding(padding)
                    .testTag(context.getString(R.string.home_movie_list_test)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                contentPadding = PaddingValues(dimensionResource(R.dimen.padding_xsmall))
            ) {
                items(homeState.movies) { movie ->
                    HomeItem(
                        onDetail = onDetail,
                        onFavourite = { homeViewModel.updateMovie(movie) },
                        movie = movie,
                        homeViewModel = homeViewModel
                    )
                }
            }
        }
    }
}