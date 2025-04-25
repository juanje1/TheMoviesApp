package com.juanje.themoviesapp.ui.screens.common.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.juanje.themoviesapp.R

@Composable
fun LogoutAlertDialog(
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        title = { Text(context.getString(R.string.logout_dialog_title)) },
        text = { Text(context.getString(R.string.logout_dialog_message)) },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(
                onClick = {
                    onAccept()
                    onDismiss()
                }
            ) {
                Text(context.getString(R.string.accept_button_dialog))
            }
        },
        dismissButton = {
            Button(
                onClick = { onCancel() }
            ) {
                Text(context.getString(R.string.cancel_button_dialog))
            }
        }
    )
}