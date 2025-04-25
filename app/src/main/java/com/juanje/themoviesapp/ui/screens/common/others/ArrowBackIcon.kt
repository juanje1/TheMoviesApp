package com.juanje.themoviesapp.ui.screens.common.others

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.juanje.themoviesapp.R

@Composable
fun ArrowBackIcon(onClickBack: (String) -> Unit, userName: String) {
    val context = LocalContext.current

    IconButton(onClick = { onClickBack(userName) }) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = context
                .getString(R.string.arrow_back_content_description)
        )
    }
}