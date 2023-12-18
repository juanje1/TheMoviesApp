package com.juanje.themoviesapp.ui.screens.register

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.User
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.initializeErrorMessages
import com.juanje.usecases.LoadUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val loadUser: LoadUser,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext val context: Context
) : ViewModel(){

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun onRegisterClick(
        userName: String, firstName: String, lastName: String, email: String, password: String
    ) {
        viewModelScope.launch {
            _state.value = UiState(
                userValid = true,
                timeExecution = _state.value.timeExecution + 1
            )
            checkUserNameValid(userName)
            checkFieldValid(context.getString(R.string.firstname_error_messages), firstName)
            checkFieldValid(context.getString(R.string.lastname_error_messages), lastName)
            checkEmailValid(email)
            checkPasswordValid(password)

            if (_state.value.userValid) {
                loadUser.invokeInsertUser(User(userName, firstName, lastName, email, password))
            }
        }
    }

    private fun checkEmptyField(field: String, text: String) {
        if (text.isEmpty()) {
            _state.value.errorMessages[field] = context.getString(R.string.field_not_empty)
            _state.value = UiState(
                timeExecution = _state.value.timeExecution,
                errorMessages = _state.value.errorMessages
            )
        }
        else resetMessageError(field)
    }

    private fun checkUserNameExists(userName: String) = runBlocking {
        val existsUserName = withContext(Dispatchers.IO) {
            loadUser.invokeExistsUserName(userName)
        }
        if (existsUserName) {
            _state.value.errorMessages[context.getString(R.string.username_error_messages)] =
                context.getString(R.string.username_exists)
            _state.value = UiState(
                timeExecution = _state.value.timeExecution,
                errorMessages = _state.value.errorMessages
            )
        }
        else resetMessageError(context.getString(R.string.username_error_messages))
    }

    private fun checkEmailExists(email: String) = runBlocking {
        val existsEmail = withContext(Dispatchers.IO) {
            loadUser.invokeExistsEmail(email)
        }
        if (existsEmail) {
            _state.value.errorMessages[context.getString(R.string.email_error_messages)] =
                context.getString(R.string.email_exists)
            _state.value = UiState(
                timeExecution = _state.value.timeExecution,
                errorMessages = _state.value.errorMessages
            )
        }
        else resetMessageError(context.getString(R.string.email_error_messages))
    }

    private fun checkMinimumLengthPassword(password: String) {
        if (password.length < 8) {
            _state.value.errorMessages[context.getString(R.string.password_error_messages)] =
                context.getString(R.string.password_length)
            _state.value = UiState(
                timeExecution = _state.value.timeExecution,
                errorMessages = _state.value.errorMessages
            )
        }
        else resetMessageError(context.getString(R.string.password_error_messages))
    }

    fun checkUserNameValid(userName: String) {
        checkEmptyField(context.getString(R.string.username_error_messages), userName)
        if (_state.value.errorMessages[context.getString(R.string.username_error_messages)]
                ?.isEmpty() == true)
            checkUserNameExists(userName)
    }

    fun checkFieldValid(field: String, text: String) {
        checkEmptyField(field, text)
    }

    fun checkEmailValid(email: String) {
        checkEmptyField(context.getString(R.string.email_error_messages), email)
        if (_state.value.errorMessages[context.getString(R.string.email_error_messages)]
                ?.isEmpty() == true)
            checkEmailExists(email)
    }

    fun checkPasswordValid(password: String) {
        checkMinimumLengthPassword(password)
    }

    private fun resetMessageError(field: String) {
        _state.value.errorMessages[field] = ""
        _state.value = UiState(
            userValid = _state.value.userValid,
            timeExecution = _state.value.timeExecution,
            errorMessages = _state.value.errorMessages
        )
    }

    fun resetState() {
        _state.value = UiState(
            errorMessages = _state.value.errorMessages
        )
    }

    data class UiState(
        val userValid: Boolean = false,
        val timeExecution: Int = 0,
        val errorMessages: MutableMap<String, String> = initializeErrorMessages()
    )
}