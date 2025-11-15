package com.juanje.themoviesapp.ui.screens.register

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.User
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.initializeErrorMessages
import com.juanje.themoviesapp.data.MainDispatcher
import com.juanje.usecases.LoadUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val loadUser: LoadUser,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @field:SuppressLint("StaticFieldLeak") @ApplicationContext val context: Context
) : ViewModel(){

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun onRegister(user: User) = viewModelScope.launch(mainDispatcher) {
        _state.value = UiState(userValid = true)

        checkUserNameValid(user.userName)
        checkEmptyField(context.getString(R.string.register_first_name_error_messages), user.firstName)
        checkEmptyField(context.getString(R.string.register_last_name_error_messages), user.lastName)
        checkEmailValid(user.email)
        checkPasswordValid(user.password)

        if (_state.value.userValid) {
            loadUser.invokeInsertUser(user)
        }

        _state.value = _state.value.copy(timeExecution = 1)
    }

    fun checkEmptyField(field: String, text: String) {
        if (text.isEmpty()) {
            _state.value.errorMessages[field] = context.getString(R.string.error_field_not_empty)
            _state.value = UiState(errorMessages = _state.value.errorMessages)
        } else resetErrorMessageState(field)
    }

    suspend fun checkUserNameValid(userName: String) {
        checkEmptyField(context.getString(R.string.register_user_name_error_messages), userName)

        if (_state.value.errorMessages[context.getString(R.string.register_user_name_error_messages)]?.isEmpty() == true) {
            val existsUserName = loadUser.invokeExistsUserName(userName)

            if (existsUserName) {
                _state.value.errorMessages[context.getString(R.string.register_user_name_error_messages)] = context.getString(R.string.error_username_exists)
                _state.value = UiState(errorMessages = _state.value.errorMessages)
            } else resetErrorMessageState(context.getString(R.string.register_user_name_error_messages))
        }
    }

    suspend fun checkEmailValid(email: String) {
        checkEmptyField(context.getString(R.string.register_email_error_messages), email)

        if (_state.value.errorMessages[context.getString(R.string.register_email_error_messages)]?.isEmpty() == true) {
            val existsEmail = loadUser.invokeExistsEmail(email)

            if (existsEmail) {
                _state.value.errorMessages[context.getString(R.string.register_email_error_messages)] = context.getString(R.string.error_email_exists)
                _state.value = UiState(errorMessages = _state.value.errorMessages)
            } else resetErrorMessageState(context.getString(R.string.register_email_error_messages))
        }
    }

    fun checkPasswordValid(password: String) =
        if (password.length < 8) {
            _state.value.errorMessages[context.getString(R.string.register_password_error_messages)] = context.getString(R.string.error_password_length)
            _state.value = UiState(errorMessages = _state.value.errorMessages)
        } else resetErrorMessageState(context.getString(R.string.register_password_error_messages))

    private fun resetErrorMessageState(field: String) {
        _state.value.errorMessages[field] = ""
        _state.value = _state.value.copy(errorMessages = _state.value.errorMessages)
    }

    fun resetState() { _state.value = UiState(errorMessages = _state.value.errorMessages) }

    data class UiState(
        val userValid: Boolean = false,
        val timeExecution: Int = 0,
        val errorMessages: MutableMap<String, String> = initializeErrorMessages()
    )
}