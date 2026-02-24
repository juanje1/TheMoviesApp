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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.PAGE_THRESHOLD
import com.juanje.themoviesapp.common.showMessage
import com.juanje.themoviesapp.ui.screens.common.dialogs.LogoutAlertDialog
import com.juanje.themoviesapp.ui.screens.common.others.MyTopAppBar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun HomeScreen(
    onLogin: () -> Unit,
    onDetail: (String, Int) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var showLogoutAlertDialog by rememberSaveable { mutableStateOf(false) }

    val homeState by homeViewModel.state.collectAsState()
    val errorMessage = homeState.error?.let { stringResource(it) }
    val listState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(listState, homeState.isGettingMovies) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .debounce(300)
            .map { index ->
                val totalItemsCount = listState.layoutInfo.totalItemsCount
                val threshold = totalItemsCount - PAGE_THRESHOLD
                totalItemsCount > 0 && index >= threshold && !homeState.isGettingMovies
            }.distinctUntilChanged()
            .filter { shouldLoad -> shouldLoad }
            .collect { homeViewModel.getAndInsertMovies(homeState.userName) }
    }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            showMessage(coroutineScope, snackBarHostState, message)
            homeViewModel.resetError()
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
                    userName = homeState.userName,
                    origin = stringResource(R.string.origin_from_home),
                    onLogout = { showLogoutAlertDialog = true }
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.testTag(stringResource(R.string.snack_bar_host_test))
            )
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = homeState.isRefreshingMovies,
            onRefresh = { homeViewModel.getAndInsertMovies(homeState.userName, true) },
            modifier = Modifier.padding(padding)
        ) {
            if (homeState.isInitialLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(stringResource(R.string.home_loading_spinner_text)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    state = listState,
                    columns = GridCells.Adaptive(dimensionResource(R.dimen.column_min_width)),
                    modifier = Modifier.testTag(stringResource(R.string.home_movie_list_test)),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                    contentPadding = PaddingValues(dimensionResource(R.dimen.padding_xsmall))
                ) {
                    items(homeState.movies) { movieFavorite ->
                        HomeItem(
                            onDetail = onDetail,
                            onFavourite = { homeViewModel.updateMovie(movieFavorite.movie) },
                            movieFavorite = movieFavorite
                        )
                    }
                }
            }
        }
    }
}