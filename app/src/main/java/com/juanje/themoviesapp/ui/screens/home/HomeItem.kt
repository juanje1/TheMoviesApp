package com.juanje.themoviesapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.IMAGE_ASPECT_RATIO

@Composable
fun HomeItem(
    onDetail: (String, String) -> Unit,
    onFavourite: () -> Unit,
    movieFavorite: MovieFavorite
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetail(movieFavorite.movie.businessId, movieFavorite.movie.userName) }
            .background(MaterialTheme.colorScheme.secondary)
            .testTag(stringResource(R.string.home_movie_list_test) +"_${movieFavorite.movie.businessId}"),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(IMAGE_ASPECT_RATIO)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            if (movieFavorite.movie.posterPath.isEmpty()) {
                Image(
                    painter = painterResource(R.drawable.no_image),
                    contentDescription = movieFavorite.movie.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                AsyncImage(
                    model = stringResource(R.string.image_url) + movieFavorite.movie.posterPath,
                    error = painterResource(R.drawable.no_image),
                    contentDescription = movieFavorite.movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag(stringResource(R.string.home_movie_image_test) + "_${movieFavorite.movie.businessId}")
                )
            }
            Icon(
                imageVector = if (movieFavorite.isFavorite) Icons.Filled.Favorite else Icons.TwoTone.Favorite,
                contentDescription = movieFavorite.movie.title,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensionResource(R.dimen.padding_xsmall))
                    .clickable { onFavourite() }
                    .testTag(stringResource(R.string.home_movie_favourite_test) + "_${movieFavorite.movie.businessId}"),
                tint = if (movieFavorite.isFavorite) Color.Red else Color.White
            )
        }
        Text(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_xsmall))
                .height(dimensionResource(R.dimen.cell_title_height))
                .align(Alignment.CenterHorizontally)
                .testTag(stringResource(R.string.home_movie_title_test) +"_${movieFavorite.movie.businessId}"),
            text = movieFavorite.movie.title,
            textAlign = TextAlign.Center,
            fontSize = dimensionResource(R.dimen.font_size_small).value.sp,
            color = MaterialTheme.colorScheme.background,
            overflow = TextOverflow.Ellipsis
        )
    }
}