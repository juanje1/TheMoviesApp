package com.juanje.themoviesapp.ui.screens.common.others

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.juanje.themoviesapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    userName: String,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current

    TopAppBar(
        title = {
            Column {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(R.drawable.profile)
                            .crossfade(true)
                            .build(),
                        contentDescription = context.getString(R.string.profile_default_image_description),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(dimensionResource(R.dimen.cell_profile_image_width))
                    )
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacer_medium)))
                    Column {
                        Text(
                            text = userName.ifEmpty { context.getString(R.string.anonymous_username_text) },
                            fontSize = dimensionResource(R.dimen.font_size_large).value.sp,
                            maxLines = context.getString(R.string.max_lines).toInt(),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_small)))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        actions = {
            IconButton(
                onClick = onLogoutClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
                    contentDescription = null
                )
            }
        }
    )
}