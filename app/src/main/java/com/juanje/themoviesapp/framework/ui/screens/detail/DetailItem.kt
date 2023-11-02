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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
                .height(400.dp)
                .aspectRatio(2 / 3f)
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Title",
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 16.dp, top = 32.dp)
        )
        Text(
            text = movie.title,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp)
        )
        Text(
            text = "Overview",
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        Text(
            text = movie.overview,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, end = 16.dp)
        )
        Text(
            text = "¿Favourite / Not Favourite?",
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp)
        )
        Text(
            text = if (movie.favourite) "Favourite" else "Not Favourite",
            modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 16.dp)
        )
    }
}