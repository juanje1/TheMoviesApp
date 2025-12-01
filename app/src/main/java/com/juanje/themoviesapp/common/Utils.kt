package com.juanje.themoviesapp.common

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import com.juanje.themoviesapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val IMAGE_ASPECT_RATIO = 2/3f
const val PAGE_THRESHOLD = 16

fun initializeErrorMessages() = mutableMapOf(
    "UserName" to "",
    "FirstName" to "",
    "LastName" to "",
    "Email" to "",
    "Password" to ""
)

fun showMessage(coroutineScope: CoroutineScope, snackBarHostState: SnackbarHostState, message: String, context: Context) {
    coroutineScope.launch {
        snackBarHostState.showSnackbar(
            message = message,
            actionLabel = context.getString(R.string.snack_bar_action_label)
        )
    }
}