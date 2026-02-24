package com.juanje.themoviesapp.ui.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.ui.theme.Purple40

@Composable
fun LoginItem(
    onLoginCheck: (String, String) -> Unit,
    onRegister: () -> Unit,
    loginState: LoginViewModel.UiState
) {
    var emailText by rememberSaveable { mutableStateOf("") }
    var passwordText by rememberSaveable { mutableStateOf("") }
    var passwordTextVisibility by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    AsyncImage(
        model = R.drawable.login,
        contentDescription = stringResource(R.string.login_image_description),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.image_height_large))
    )
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = dimensionResource(R.dimen.padding_image_large)),
        color = Color.White,
        shape = RoundedCornerShape(
            topStartPercent = dimensionResource(R.dimen.surface_rounded_corner).value.toInt(),
            topEndPercent = dimensionResource(R.dimen.surface_rounded_corner).value.toInt()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                text = stringResource(R.string.login_title_text),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.padding_large)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spaced_by))
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_small)
                        )
                        .testTag(stringResource(R.string.login_email_test)),
                    value = emailText,
                    onValueChange = { newEmail -> emailText = newEmail },
                    maxLines = stringResource(R.string.max_lines).toInt(),
                    singleLine = true,
                    label = { Text(text = stringResource(R.string.login_email_text)) },
                    shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_small)),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_small)
                        )
                        .testTag(stringResource(R.string.login_password_test)),
                    value = passwordText,
                    onValueChange = { newPassword -> passwordText = newPassword },
                    maxLines = stringResource(R.string.max_lines).toInt(),
                    singleLine = true,
                    label = { Text(stringResource(R.string.login_password_text)) },
                    shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_small)),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = { passwordTextVisibility = !passwordTextVisibility }
                        ) {
                            Icon(
                                imageVector =
                                    if (passwordTextVisibility) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                contentDescription = stringResource(R.string.login_password_text_visibility_description)
                            )
                        }
                    },
                    visualTransformation =
                        if (passwordTextVisibility) VisualTransformation.None
                        else PasswordVisualTransformation()
                )
            }
            Spacer(modifier = Modifier.weight(0.4f))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.padding_large)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.button_width_medium))
                        .height(dimensionResource(R.dimen.button_height_medium))
                        .testTag(stringResource(R.string.login_login_test)),
                    onClick = { onLoginCheck(emailText, passwordText) },
                    shape = RoundedCornerShape(dimensionResource(R.dimen.shape_rounded_corner_medium).value.toInt()),
                    enabled = !loginState.isLogging,
                ) {
                    Text(
                        text = stringResource(R.string.login_login_button).uppercase()
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Text(
                modifier = Modifier
                    .clickable { onRegister() }
                    .testTag(stringResource(R.string.login_register_test)),
                text = AnnotatedString(stringResource(R.string.login_register_text)),
                style = TextStyle(
                    fontSize = dimensionResource(R.dimen.font_size_small).value.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = Purple40
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacer_large)))
        }
    }
}