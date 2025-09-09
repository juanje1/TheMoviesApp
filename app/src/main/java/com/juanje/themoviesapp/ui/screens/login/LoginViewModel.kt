package com.juanje.themoviesapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.User
import com.juanje.usecases.LoadUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loadUser: LoadUser) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun onLoginClick(email: String, password: String) = viewModelScope.launch {
        val user = loadUser.invokeGetUser(email, password)

        _state.value = UiState(
            user = user,
            timeExecution = state.value.timeExecution+1,
            isUserValid = user.getIsUserValid(email, password)
        )
    }

    fun resetState() { _state.value = UiState() }

    private fun User?.getIsUserValid(email: String, password: String) : Boolean =
        this?.email == email && this.password == password

    data class UiState(
        val user: User ?= null,
        val timeExecution: Int = 0,
        val isUserValid: Boolean = false
    )
}