package com.juanje.themoviesapp.ui.screens.detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextDecoration
import coil.compose.AsyncImage
import com.juanje.domain.Movie
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.ImageAspectRatio

@Composable
fun DetailItem(
    padding: PaddingValues,
    movie: Movie
) {
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
                .padding(top = dimensionResource(R.dimen.padding_medium))
                .height(dimensionResource(R.dimen.cell_image_medium))
                .border(
                    width = dimensionResource(R.dimen.border_medium),
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_small)))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_small)))
                .aspectRatio(ImageAspectRatio)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )
        Text(
            text = context.getString(R.string.detail_title),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                top = dimensionResource(R.dimen.padding_xlarge)
            )
        )
        Text(
            text = movie.title ?: "",
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                top = dimensionResource(R.dimen.padding_xsmall),
                end = dimensionResource(R.dimen.padding_medium)
            )
        )
        Text(
            text = context.getString(R.string.detail_overview),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                top = dimensionResource(R.dimen.padding_medium)
            )
        )
        Text(
            text = movie.overview ?: "",
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                top = dimensionResource(R.dimen.padding_xsmall),
                end = dimensionResource(R.dimen.padding_medium)
            )
        )
        Text(
            text = context.getString(R.string.detail_favourite_not_favourite),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                top = dimensionResource(R.dimen.padding_medium)
            )
        )
        Text(
            text =
                if (movie.favourite) context.getString(R.string.detail_favourite)
                else context.getString(R.string.detail_not_favourite),
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.padding_medium),
                top = dimensionResource(R.dimen.padding_xsmall),
                bottom = dimensionResource(R.dimen.padding_medium)
            )
        )
    }
}