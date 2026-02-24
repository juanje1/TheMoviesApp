package com.juanje.themoviesapp.ui.screens.login

import androidx.lifecycle.SavedStateHandle
import com.juanje.themoviesapp.ui.navigation.Screen
import com.juanje.themoviesapp.ui.screens.common.CoroutinesTestRule
import com.juanje.themoviesapp.ui.screens.common.FakeAppIdlingResource
import com.juanje.themoviesapp.ui.screens.common.UserMother
import com.juanje.usecases.LoadUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {
    private lateinit var loginViewModel: LoginViewModel

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var loadUser: LoadUser

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        val savedStateHandle = SavedStateHandle(mapOf(
            Screen.Login::registered.name to true
        ))

        loginViewModel = LoginViewModel(
            loadUser = loadUser,
            idlingResource = FakeAppIdlingResource(),
            mainDispatcher = coroutinesTestRule.testDispatcher,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `Correct login with valid credentials`() = runTest {
        // Given
        val validUser = UserMother.createUser()
        whenever(loadUser.invokeGetUser(any(), any())).thenReturn(validUser)

        // When
        loginViewModel.onLogin(validUser.email, validUser.password)
        advanceUntilIdle()

        // Then
        assertTrue(loginViewModel.state.value.isUserValid)
        verify(loadUser).invokeGetUser(validUser.email, validUser.password)
    }

    @Test
    fun `Incorrect login with invalid credentials`() = runTest {
        // Given
        val validUser = UserMother.createUser()
        val emptyUser = UserMother.createUser(email = "", password = "")
        whenever(loadUser.invokeGetUser(any(), any())).thenReturn(validUser)

        // When
        loginViewModel.onLogin(emptyUser.email, emptyUser.password)
        advanceUntilIdle()

        // Then
        assertFalse(loginViewModel.state.value.isUserValid)
        verify(loadUser).invokeGetUser(emptyUser.email, emptyUser.password)
    }
}