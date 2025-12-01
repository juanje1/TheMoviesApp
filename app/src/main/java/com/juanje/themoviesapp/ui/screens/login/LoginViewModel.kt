package com.juanje.themoviesapp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanje.domain.User
import com.juanje.themoviesapp.data.MainDispatcher
import com.juanje.themoviesapp.utils.EspressoIdlingResource
import com.juanje.usecases.LoadUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loadUser: LoadUser,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun onLogin(email: String, password: String) = viewModelScope.launch(mainDispatcher) {
        EspressoIdlingResource.increment()

        try {
            val user = loadUser.invokeGetUser(email, password)

            _state.value = UiState(
                user = user,
                timeExecution = state.value.timeExecution + 1,
                isUserValid = user.getIsUserValid(email, password)
            )
        } finally {
            EspressoIdlingResource.decrement()
        }
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