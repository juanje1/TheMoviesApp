package com.juanje.themoviesapp.ui.screens.common

import android.content.Context
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import com.juanje.themoviesapp.R

@Composable
fun ArrowBackIcon(onClickBack: () -> Unit, context: Context) {
    IconButton(onClick = { onClickBack() }) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = context
                .getString(R.string.arrow_back_content_description)
        )
    }
}