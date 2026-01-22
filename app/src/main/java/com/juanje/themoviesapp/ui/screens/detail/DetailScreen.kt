package com.juanje.themoviesapp.ui.screens.detail

import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
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
    userName: String,
    movieId: Int
) {
    var showLogoutAlertDialog by rememberSaveable { mutableStateOf(false) }

    val detailViewModel: DetailViewModel = hiltViewModel()
    val detailState by detailViewModel.state.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        detailViewModel.setArgsFlow(userName, movieId)
    }

    LaunchedEffect(detailState.error) {
        detailState.error?.let { resId ->
            showMessage(coroutineScope, snackBarHostState, context.getString(resId))
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
                val titleMovie: String =
                    if (movieFavorite.movie.title == null) context.getString(R.string.anonymous_title_movie_text)
                    else movieFavorite.movie.title!!.ifEmpty { context.getString(R.string.anonymous_title_movie_text) }

                Surface(shadowElevation = dimensionResource(R.dimen.shadow_elevation_topBar)) {
                    MyTopAppBar(
                        userName = userName,
                        titleMovie = titleMovie,
                        origin = context.getString(R.string.origin_from_other),
                        onBack = { navController.popBackStack() },
                        onLogout = { showLogoutAlertDialog = true }
                    )
                }
            }
        ) { padding ->
            DetailItem(
                padding = padding,
                movieFavorite = movieFavorite,
                detailViewModel = detailViewModel
            )
        }
    }
}