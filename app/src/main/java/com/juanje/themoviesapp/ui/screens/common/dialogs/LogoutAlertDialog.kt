package com.juanje.themoviesapp.ui.screens.common.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.juanje.themoviesapp.R

@Composable
fun LogoutAlertDialog(
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text(stringResource(R.string.logout_dialog_title)) },
        text = { Text(stringResource(R.string.logout_dialog_message)) },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    onAccept()
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.accept_button_dialog))
            }
        },
        dismissButton = {
            Button(
                onClick = { onCancel() }
            ) {
                Text(stringResource(R.string.cancel_button_dialog))
            }
        }
    )
}