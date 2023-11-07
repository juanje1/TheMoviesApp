package com.juanje.themoviesapp.framework.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.domain.Movie

@Composable
fun HomeItem(movie: Movie, navController: NavHostController, onClick: () -> Unit) {
    Column(
        modifier = Modifier.background(MaterialTheme.colors.secondary)
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    navController.navigate("detail/${movie.id}")
                }
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w185/${movie.posterPath}",
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2 / 3f)
            )
            var color: Color = Color.White
            if(movie.favourite) color = Color.Red
            Icon(
                imageVector = Icons.TwoTone.Favorite,
                contentDescription = movie.title,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensionResource(R.dimen.padding_xsmall))
                    .clickable { onClick() },
                tint = color
            )
        }
        Text(
            text = movie.title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_xsmall))
                .height(dimensionResource(R.dimen.cell_title_height))
                .align(Alignment.CenterHorizontally)
        )
    }
}