package com.juanje.themoviesapp.ui.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextDecoration
import coil.compose.AsyncImage
import com.juanje.themoviesapp.R
import com.juanje.domain.Movie
import com.juanje.themoviesapp.common.ImageAspectRatio

@Composable
fun DetailItem(padding: PaddingValues, movie: Movie) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(padding)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = context.getString(R.string.image_url)+movie.posterPath,
            contentDescription = movie.title,
            modifier = Modifier
                .height(dimensionResource(R.dimen.cell_image_height))
                .aspectRatio(ImageAspectRatio)
                .align(Alignment.CenterHorizontally)
                .padding(top = dimensionResource(R.dimen.padding_small)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = context.getString(R.string.title_text),
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
            text = context.getString(R.string.overview_text),
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
            text = context.getString(R.string.favourite_not_favourite_text),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_small)
            )
        )
        Text(
            text =
                if (movie.favourite) context.getString(R.string.favourite_text)
                else context.getString(R.string.not_favourite_text),
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_small),
                top = dimensionResource(R.dimen.padding_xsmall),
                bottom = dimensionResource(R.dimen.padding_small)
            )
        )
    }
}