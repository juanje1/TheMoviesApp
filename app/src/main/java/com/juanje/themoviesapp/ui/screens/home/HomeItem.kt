package com.juanje.themoviesapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.juanje.domain.dataclasses.MovieFavorite
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.IMAGE_ASPECT_RATIO

@Composable
fun HomeItem(
    onDetail: (String, Int) -> Unit,
    onFavourite: () -> Unit,
    movieFavorite: MovieFavorite
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .clickable { onDetail(movieFavorite.movie.userName, movieFavorite.movie.id) }
            .background(MaterialTheme.colorScheme.secondary)
            .testTag(context.getString(R.string.home_movie_list_test)+"_${movieFavorite.movie.id}"),
    ) {
        Box {
            AsyncImage(
                model = context.getString(R.string.image_url)+movieFavorite.movie.posterPath,
                contentDescription = movieFavorite.movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(IMAGE_ASPECT_RATIO)
                    .testTag(context.getString(R.string.home_movie_image_test)+"_${movieFavorite.movie.id}")
            )
            var color: Color = Color.White
            if (movieFavorite.isFavorite) color = Color.Red
            Icon(
                imageVector = Icons.TwoTone.Favorite,
                contentDescription = movieFavorite.movie.title,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensionResource(R.dimen.padding_xsmall))
                    .clickable { onFavourite() }
                    .testTag(context.getString(R.string.home_movie_favourite_test)+"_${movieFavorite.movie.id}"),
                tint = color
            )
        }
        Text(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_xsmall))
                .height(dimensionResource(R.dimen.cell_title_height))
                .align(Alignment.CenterHorizontally)
                .testTag(context.getString(R.string.home_movie_title_test)+"_${movieFavorite.movie.id}"),
            text = movieFavorite.movie.title ?: "",
            textAlign = TextAlign.Center,
            fontSize = dimensionResource(R.dimen.font_size_small).value.sp,
            color = MaterialTheme.colorScheme.background,
            overflow = TextOverflow.Ellipsis
        )
    }
}