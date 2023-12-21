package com.juanje.themoviesapp.common

import android.content.Context
import android.widget.Toast

const val ImageAspectRatio = 2/3f

fun initializeErrorMessages() = mutableMapOf(
    "UserName" to "",
    "FirstName" to "",
    "LastName" to "",
    "Email" to "",
    "Password" to ""
)

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}