package com.juanje.themoviesapp.ui.screens.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.User
import com.juanje.themoviesapp.common.initializeErrorMessages
import com.juanje.usecases.LoadUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val loadUser: LoadUser) : ViewModel() {

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
            checkFieldValid("FirstName", firstName)
            checkFieldValid("LastName", lastName)
            checkEmailValid(email)
            checkPasswordValid(password)

            if (_state.value.userValid) {
                loadUser.invokeInsertUser(User(userName, firstName, lastName, email, password))
            }
        }
    }

    private fun checkEmptyField(field: String, text: String) {
        if (text.isEmpty()) {
            _state.value.errorMessages[field] = "The field cannot be empty"
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
            _state.value.errorMessages["UserName"] = "The UserName already exists"
            _state.value = UiState(
                timeExecution = _state.value.timeExecution,
                errorMessages = _state.value.errorMessages
            )
        }
        else resetMessageError("UserName")
    }

    private fun checkEmailExists(email: String) = runBlocking {
        val existsEmail = withContext(Dispatchers.IO) {
            loadUser.invokeExistsEmail(email)
        }
        if (existsEmail) {
            _state.value.errorMessages["Email"] = "The Email already exists"
            _state.value = UiState(
                timeExecution = _state.value.timeExecution,
                errorMessages = _state.value.errorMessages
            )
        }
        else resetMessageError("Email")
    }

    private fun checkMinimumLengthPassword(password: String) {
        if (password.length < 8) {
            _state.value.errorMessages["Password"] = "The Password must have at least 8 characters"
            _state.value = UiState(
                timeExecution = _state.value.timeExecution,
                errorMessages = _state.value.errorMessages
            )
        }
        else resetMessageError("Password")
    }

    fun checkUserNameValid(userName: String) {
        checkEmptyField("UserName", userName)
        if (_state.value.errorMessages["UserName"]?.isEmpty() == true)
            checkUserNameExists(userName)
    }

    fun checkFieldValid(field: String, text: String) {
        checkEmptyField(field, text)
    }

    fun checkEmailValid(email: String) {
        checkEmptyField("Email", email)
        if (_state.value.errorMessages["Email"]?.isEmpty() == true)
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