package com.juanje.themoviesapp.ui.screens.register

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
    private lateinit var registerViewModel: RegisterViewModel

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var loadUser: LoadUser

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(
            loadUser = loadUser,
            idlingResource = FakeAppIdlingResource(),
            mainDispatcher = coroutinesTestRule.testDispatcher
        )
    }

    @Test
    fun `Correct register with valid user`() = runTest {
        // Given
        val validUser = UserMother.createUser()
        whenever(loadUser.invokeExistsUserName(any())).thenReturn(false)
        whenever(loadUser.invokeExistsEmail(any())).thenReturn(false)

        // When
        registerViewModel.onRegister(validUser)
        advanceUntilIdle()

        // Then
        assertTrue(registerViewModel.state.value.userValid)
        verify(loadUser).invokeExistsUserName(validUser.userName)
        verify(loadUser).invokeExistsEmail(validUser.email)
    }

    @Test
    fun `Incorrect register with existing userName`() = runTest {
        // Given
        val invalidUser = UserMother.createUser()
        whenever(loadUser.invokeExistsUserName(any())).thenReturn(true)
        whenever(loadUser.invokeExistsEmail(any())).thenReturn(false)

        // When
        registerViewModel.onRegister(invalidUser)
        advanceUntilIdle()

        // Then
        assertFalse(registerViewModel.state.value.userValid)
        verify(loadUser).invokeExistsUserName(invalidUser.userName)
        verify(loadUser).invokeExistsEmail(invalidUser.email)
    }

    @Test
    fun `Incorrect register with existing email`() = runTest {
        // Given
        val invalidUser = UserMother.createUser()
        whenever(loadUser.invokeExistsUserName(any())).thenReturn(false)
        whenever(loadUser.invokeExistsEmail(any())).thenReturn(true)

        // When
        registerViewModel.onRegister(invalidUser)
        advanceUntilIdle()

        // Then
        assertFalse(registerViewModel.state.value.userValid)
        verify(loadUser).invokeExistsUserName(invalidUser.userName)
        verify(loadUser).invokeExistsEmail(invalidUser.email)
    }

    @Test
    fun `Incorrect register with some empty field`() = runTest {
        // Given
        val invalidUser = UserMother.createUser(userName = "")

        // When
        registerViewModel.onRegister(invalidUser)
        advanceUntilIdle()

        // Then
        assertFalse(registerViewModel.state.value.userValid)
        verify(loadUser, never()).invokeExistsUserName(invalidUser.userName)
        verify(loadUser, never()).invokeExistsEmail(invalidUser.email)
    }
}