package com.juanje.themoviesapp.ui.screens.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.juanje.themoviesapp.R

@Composable
fun DetailScreen(navController: NavHostController, userName: String, movieId: Int) {
    val viewModel: DetailViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    if (state.isInit) {
        viewModel.resetInit()
        viewModel.getMovieDetail(userName, movieId)
    }

    state.movie?.let {
        Scaffold(topBar = {
            TopAppBar {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = context.getString(R.string.arrow_back_content_description),
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                Text(text = it.title)
            }
        }) { padding ->
            DetailItem(padding, it)
        }
    }
}