package com.juanje.themoviesapp.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.MainDispatcher
import com.juanje.domain.common.RegistrationField
import com.juanje.domain.dataclasses.User
import com.juanje.themoviesapp.R
import com.juanje.themoviesapp.common.AppIdlingResource
import com.juanje.themoviesapp.common.createHandler
import com.juanje.themoviesapp.common.trackLoading
import com.juanje.usecases.LoadUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val loadUser: LoadUser,
    private val idlingResource: AppIdlingResource,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel(){

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    private var searchJob: Job? = null

    private fun registerHandler(onCleanup: () -> Unit = {}) = createHandler(
        onUpdateError = { errorRes -> _state.update { it.copy(error = errorRes) } },
        onCleanup = onCleanup
    )

    fun onRegister() = viewModelScope.launch(mainDispatcher + registerHandler {
        _state.update { it.copy(isRegistering = false) }
    }) {
        if (_state.value.isRegistering) return@launch

        _state.update { it.copy(isRegistering = true, actionFinished = false) }

        val localErrors = validateLocalErrors(_state.value.user)
        _state.update { it.copy(errorMessages = localErrors, userValid = localErrors.isEmpty()) }

        trackLoading(idlingResource = idlingResource) {
            if (_state.value.userValid) {
                val remoteErrors = validateRemoteErrors(_state.value.user)
                _state.update { it.copy(errorMessages = remoteErrors, userValid = remoteErrors.isEmpty()) }

                if (_state.value.userValid) {
                    loadUser.invokeInsertUser(_state.value.user)
                }
            }
        }
        _state.update { it.copy(isRegistering = false, actionFinished = true) }
    }

    fun onFieldChanged(field: RegistrationField, text: String) {
        setFieldChanged(field, text)
        searchJob?.cancel()

        val localError = when (field) {
            RegistrationField.PASSWORD -> if (text.length < 8) R.string.error_password_length else null
            else -> if (text.isEmpty()) R.string.error_field_not_empty else null
        }

        if (localError != null) {
            updateError(field, localError)
            return
        }

        clearFieldError(field)

        if (field == RegistrationField.USER_NAME || field == RegistrationField.EMAIL) {
            searchJob = viewModelScope.launch(mainDispatcher + registerHandler()) {
                delay(400)
                if (field == RegistrationField.USER_NAME) {
                    existsUserName(field, text)
                } else {
                    existsEmail(field, text)
                }
            }
        }
    }

    private fun validateLocalErrors(user: User): MutableMap<RegistrationField, Int> =
        mutableMapOf<RegistrationField, Int>().apply {
            if (user.userName.isEmpty()) put(RegistrationField.USER_NAME, R.string.error_field_not_empty)
            if (user.firstName.isEmpty()) put(RegistrationField.FIRST_NAME, R.string.error_field_not_empty)
            if (user.lastName.isEmpty()) put(RegistrationField.LAST_NAME, R.string.error_field_not_empty)
            if (user.email.isEmpty()) put(RegistrationField.EMAIL, R.string.error_field_not_empty)
            if (user.password.length < 8) put(RegistrationField.PASSWORD, R.string.error_password_length)
        }

    private suspend fun validateRemoteErrors(user: User): MutableMap<RegistrationField, Int> {
        val remoteErrors = _state.value.errorMessages.toMutableMap()

        if (loadUser.invokeExistsUserName(user.userName))
            remoteErrors[RegistrationField.USER_NAME] = R.string.error_username_exists

        if (loadUser.invokeExistsEmail(user.email))
            remoteErrors[RegistrationField.EMAIL] = R.string.error_email_exists

        return remoteErrors
    }

    private fun updateError(field: RegistrationField, errorRes: Int) {
        _state.update { currentState ->
            if (currentState.errorMessages[field] == errorRes) return@update currentState

            val newErrors = currentState.errorMessages.toMutableMap()
            newErrors[field] = errorRes

            currentState.copy(errorMessages = newErrors, userValid = false)
        }
    }

    private fun clearFieldError(field: RegistrationField) {
        _state.update { currentState ->
            if (!currentState.errorMessages.containsKey(field)) return@update currentState

            val newErrors = currentState.errorMessages.toMutableMap()
            newErrors.remove(field)

            currentState.copy(errorMessages = newErrors, userValid = newErrors.isEmpty())
        }
    }

    private suspend fun existsUserName(field: RegistrationField, text: String) {
        trackLoading(idlingResource = idlingResource) {
            val exists = loadUser.invokeExistsUserName(text)

            if (exists) {
                updateError(field, R.string.error_username_exists)
            } else {
                clearFieldError(field)
            }
        }
    }

    private suspend fun existsEmail(field: RegistrationField, text: String) {
        trackLoading(idlingResource = idlingResource) {
            val exists = loadUser.invokeExistsEmail(text)

            if (exists) {
                updateError(field, R.string.error_email_exists)
            } else {
                clearFieldError(field)
            }
        }
    }

    private fun setFieldChanged(field: RegistrationField, text: String) {
        _state.update { state ->
            state.copy(
                user = when (field) {
                    RegistrationField.USER_NAME -> state.user.copy(userName = text)
                    RegistrationField.FIRST_NAME -> state.user.copy(firstName = text)
                    RegistrationField.LAST_NAME -> state.user.copy(lastName = text)
                    RegistrationField.EMAIL -> state.user.copy(email = text)
                    RegistrationField.PASSWORD -> state.user.copy(password = text)
                }
            )
        }
    }

    fun resetActionFinished() {
        _state.update { it.copy(actionFinished = false) }
    }

    fun resetError() {
        _state.update { it.copy(error = null) }
    }

    data class UiState(
        val user: User = User(),
        val actionFinished: Boolean = false,
        val userValid: Boolean = false,
        val isRegistering: Boolean = false,
        val errorMessages: Map<RegistrationField, Int> = emptyMap(),
        val error: Int? = null
    )
}