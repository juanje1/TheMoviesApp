package com.juanje.themoviesapp.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.extensions.showMessage
import com.juanje.themoviesapp.ui.screens.common.dialogs.LogoutAlertDialog
import com.juanje.themoviesapp.ui.screens.common.others.MyTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogin: () -> Unit,
    onDetail: (String, String, String) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    var showLogoutAlertDialog by rememberSaveable { mutableStateOf(false) }

    val movies = remember { homeViewModel.movies }.collectAsLazyPagingItems()
    val isRefreshing = movies.loadState.refresh is LoadState.Loading

    val homeState by homeViewModel.state.collectAsState()
    val errorMessage = homeState.error?.let { stringResource(it) }
    val itemsContentType = stringResource(R.string.home_items_content_type)
    val listState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(homeState.isInternetAvailable) {
        if (homeState.isInternetAvailable && movies.loadState.hasError) {
            movies.retry()
        }
    }

    LaunchedEffect(movies.loadState) {
        val errorState = movies.loadState.refresh as? LoadState.Error
            ?: movies.loadState.append as? LoadState.Error
            ?: movies.loadState.prepend as? LoadState.Error

        errorState?.let {
            homeViewModel.handlePagingError(it.error)
        }
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
            isRefreshing = isRefreshing,
            onRefresh = { movies.refresh() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
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
                    items(
                        movies.itemCount,
                        key = movies.itemKey { it.movie.businessId },
                        contentType = { itemsContentType }
                    ) { index ->
                        val movieFavorite = movies[index]

                        if (movieFavorite != null) {
                            HomeItem(
                                onDetail = { onDetail(movieFavorite.movie.businessId, movieFavorite.movie.userName, movieFavorite.movie.category) },
                                onFavourite = { homeViewModel.updateMovie(movieFavorite.movie, movieFavorite.isFavorite) },
                                movieFavorite = movieFavorite
                            )
                        }
                    }
                }
            }
        }
    }
}