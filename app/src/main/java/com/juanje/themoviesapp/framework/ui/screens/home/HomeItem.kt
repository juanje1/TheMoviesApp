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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.juanje.themoviesapp.domain.Movie

@Composable
fun HomeItem(movie: Movie, onClick: () -> Unit) {
    Column(
        modifier = Modifier.background(MaterialTheme.colors.secondary)
    ) {
        Box {
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
                    .padding(4.dp)
                    .clickable { onClick() },
                tint = color
            )
        }
        Text(
            text = movie.title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .padding(4.dp)
                .height(64.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}