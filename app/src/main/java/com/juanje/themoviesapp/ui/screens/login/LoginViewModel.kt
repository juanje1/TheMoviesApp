package com.juanje.themoviesapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.User
import com.juanje.themoviesapp.common.AppIdlingResource
import com.juanje.themoviesapp.common.toErrorRes
import com.juanje.themoviesapp.common.trackLoading
import com.juanje.themoviesapp.data.MainDispatcher
import com.juanje.usecases.LoadUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loadUser: LoadUser,
    private val idlingResource: AppIdlingResource,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun onLogin(email: String, password: String) = viewModelScope.launch(mainDispatcher) {
        trackLoading(
            idlingResource = idlingResource,
            onError = { e -> _state.update { it.copy(error = e.toErrorRes()) } }
        ) {
            val user = loadUser.invokeGetUser(email, password)

            _state.value = UiState(
                user = user,
                timeExecution = state.value.timeExecution + 1,
                isUserValid = user.getIsUserValid(email, password)
            )
        }
    }

    private fun User?.getIsUserValid(email: String, password: String) : Boolean =
        this?.email == email && this.password == password

    fun resetError() {
        _state.update { it.copy(error = null) }
    }

    fun resetState() {
        _state.value = UiState()
    }

    data class UiState(
        val user: User ?= null,
        val timeExecution: Int = 0,
        val isUserValid: Boolean = false,
        val error: Int? = null
    )
}