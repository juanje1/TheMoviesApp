package com.juanje.themoviesapp.ui.screens.register

import com.juanje.domain.common.RegistrationField
import com.juanje.domain.dataclasses.User
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
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock private lateinit var loadUser: LoadUser

    private lateinit var validUser: User
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        validUser = UserMother.createUser()

        registerViewModel = RegisterViewModel(
            loadUser = loadUser,
            idlingResource = FakeAppIdlingResource(),
            mainDispatcher = coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun `Correct register with valid user`() = runTest {
        // Given
        whenever(loadUser.invokeExistsUserName(any())).thenReturn(false)
        whenever(loadUser.invokeExistsEmail(any())).thenReturn(false)
        fillFormWith(validUser)

        // When
        registerViewModel.onRegister()
        advanceUntilIdle()

        // Then
        assertTrue(registerViewModel.state.value.userValid)
        verify(loadUser).invokeExistsUserName(validUser.userName)
        verify(loadUser).invokeExistsEmail(validUser.email)
    }

    @Test
    fun `Incorrect register with existing userName`() = runTest {
        // Given
        whenever(loadUser.invokeExistsUserName(any())).thenReturn(true)
        whenever(loadUser.invokeExistsEmail(any())).thenReturn(false)
        fillFormWith(validUser)

        // When
        registerViewModel.onRegister()
        advanceUntilIdle()

        // Then
        assertFalse(registerViewModel.state.value.userValid)
        verify(loadUser).invokeExistsUserName(validUser.userName)
        verify(loadUser).invokeExistsEmail(validUser.email)
    }

    @Test
    fun `Incorrect register with existing email`() = runTest {
        // Given
        whenever(loadUser.invokeExistsUserName(any())).thenReturn(false)
        whenever(loadUser.invokeExistsEmail(any())).thenReturn(true)
        fillFormWith(validUser)

        // When
        registerViewModel.onRegister()
        advanceUntilIdle()

        // Then
        assertFalse(registerViewModel.state.value.userValid)
        verify(loadUser).invokeExistsUserName(validUser.userName)
        verify(loadUser).invokeExistsEmail(validUser.email)
    }

    @Test
    fun `Incorrect register with some empty field`() = runTest {
        // Given
        val emptyUser = UserMother.createUser(email = "", password = "")
        fillFormWith(emptyUser)

        // When
        registerViewModel.onRegister()
        advanceUntilIdle()

        // Then
        assertFalse(registerViewModel.state.value.userValid)
        verify(loadUser, never()).invokeExistsUserName(emptyUser.userName)
        verify(loadUser, never()).invokeExistsEmail(emptyUser.email)
    }

    private fun fillFormWith(user: User) {
        registerViewModel.onFieldChanged(RegistrationField.USER_NAME, user.userName)
        registerViewModel.onFieldChanged(RegistrationField.FIRST_NAME, user.firstName)
        registerViewModel.onFieldChanged(RegistrationField.LAST_NAME, user.lastName)
        registerViewModel.onFieldChanged(RegistrationField.EMAIL, user.email)
        registerViewModel.onFieldChanged(RegistrationField.PASSWORD, user.password)
    }
}