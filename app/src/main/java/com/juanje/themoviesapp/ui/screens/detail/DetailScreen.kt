package com.juanje.themoviesapp.ui.screens.detail

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.showMessage
import com.juanje.themoviesapp.ui.screens.common.dialogs.LogoutAlertDialog
import com.juanje.themoviesapp.ui.screens.common.others.MyTopAppBar

@Composable
fun DetailScreen(
    navController: NavHostController,
    onLogin: () -> Unit,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    var showLogoutAlertDialog by rememberSaveable { mutableStateOf(false) }

    val detailState by detailViewModel.state.collectAsState()
    val errorMessage = detailState.error?.let { stringResource(it) }
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            showMessage(coroutineScope, snackBarHostState, message)
            detailViewModel.resetError()
        }
    }

    if (showLogoutAlertDialog) {
        LogoutAlertDialog(
            onAccept = { onLogin() },
            onCancel = { showLogoutAlertDialog = false },
            onDismiss = { showLogoutAlertDialog = false }
        )
    }

    detailState.movie?.let { movieFavorite ->
        Scaffold(
            topBar = {
                val titleMovie: String = movieFavorite.movie.title
                    .ifEmpty { stringResource(R.string.anonymous_title_movie_text) }

                Surface(shadowElevation = dimensionResource(R.dimen.shadow_elevation_topBar)) {
                    MyTopAppBar(
                        userName = detailState.userName,
                        titleMovie = titleMovie,
                        origin = stringResource(R.string.origin_from_other),
                        onBack = { navController.popBackStack() },
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
            DetailItem(
                movieFavorite = movieFavorite,
                padding = padding
            )
        }
    }
}