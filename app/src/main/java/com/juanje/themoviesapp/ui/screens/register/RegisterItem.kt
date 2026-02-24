package com.juanje.themoviesapp.ui.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.juanje.domain.common.RegistrationField
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.screens.common.fields.EmailField
import com.juanje.themoviesapp.ui.screens.common.fields.FirstNameField
import com.juanje.themoviesapp.ui.screens.common.fields.LastNameField
import com.juanje.themoviesapp.ui.screens.common.fields.PasswordField
import com.juanje.themoviesapp.ui.screens.common.fields.UserNameField

@Composable
fun RegisterItem(
    onRegister: () -> Unit,
    onCancel: () -> Unit,
    onFieldChanged: (RegistrationField, String) -> Unit,
    registerState: RegisterViewModel.UiState
) {
    AsyncImage(
        model = R.drawable.register,
        contentDescription = stringResource(R.string.register_image_description),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.image_height_medium))
    )
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dimensionResource(R.dimen.padding_image_medium)),
        color = Color.White,
        shape = RoundedCornerShape(
            topStartPercent = dimensionResource(R.dimen.surface_rounded_corner).value.toInt(),
            topEndPercent = dimensionResource(R.dimen.surface_rounded_corner).value.toInt()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spaced_by))
        ) {
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
            Text(
                text = stringResource(R.string.register_title_text),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_medium)))

            UserNameField(onFieldChanged, registerState)
            FirstNameField(onFieldChanged, registerState)
            LastNameField(onFieldChanged, registerState)
            EmailField(onFieldChanged, registerState)
            PasswordField(onFieldChanged, registerState)

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = dimensionResource(R.dimen.padding_large)),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.button_width_small))
                        .height(dimensionResource(R.dimen.button_height_medium))
                        .testTag(stringResource(R.string.register_register_test)),
                    onClick = onRegister,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_medium)),
                    enabled = !registerState.isRegistering,
                ) {
                    Text(
                        text = stringResource(R.string.register_button).uppercase()
                    )
                }
                Button(
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.button_width_small))
                        .height(dimensionResource(R.dimen.button_height_medium)),
                    onClick = { onCancel() },
                    shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_medium))
                ) {
                    Text(
                        text = stringResource(R.string.cancel_button).uppercase()
                    )
                }
            }
        }
    }
}