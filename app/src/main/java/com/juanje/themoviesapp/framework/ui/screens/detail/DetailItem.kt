package com.juanje.themoviesapp.framework.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextDecoration
import coil.compose.AsyncImage
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.domain.Movie

@Composable
fun DetailItem(padding: PaddingValues, movie: Movie) {
    Column(
        modifier = Modifier
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w185/${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier
                .height(dimensionResource(R.dimen.cell_image_height))
                .aspectRatio(2 / 3f)
                .align(Alignment.CenterHorizontally)
                .padding(top = dimensionResource(R.dimen.padding_small)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Title",
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_medium)
            )
        )
        Text(
            text = movie.title,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_xsmall),
                end = dimensionResource(R.dimen.padding_small)
            )
        )
        Text(
            text = "Overview",
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_small)
            )
        )
        Text(
            text = movie.overview,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_xsmall),
                end = dimensionResource(R.dimen.padding_small)
            )
        )
        Text(
            text = "¿Favourite / Not Favourite?",
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_small)
            )
        )
        Text(
            text = if (movie.favourite) "Favourite" else "Not Favourite",
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_xsmall),
                bottom = dimensionResource(R.dimen.padding_small)
            )
        )
    }
}