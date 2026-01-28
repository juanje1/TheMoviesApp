package com.juanje.data.repositories

import android.database.sqlite.SQLiteException
import com.juanje.data.datasources.UserLocalDataSource
import com.juanje.domain.common.AppError
import com.juanje.domain.dataclasses.User
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {
    companion object {
        private const val wrongEmail: String = "wrong@email.com"
        private const val wrongPassword: String = "1234"
    }

    private lateinit var userRepository: UserRepositoryImpl

    @Mock
    private lateinit var userLocalDataSource: UserLocalDataSource

    @Before
    fun setup() {
        userRepository = UserRepositoryImpl(userLocalDataSource)
    }

    @Test
    fun `When the user does not exist in the database, getUser returns null`() = runTest {
        // Given
        whenever(userLocalDataSource.getUser(wrongEmail, wrongPassword)).thenReturn(null)

        // When
        val result = userRepository.getUser(wrongEmail, wrongPassword)

        // Then
        verify(userLocalDataSource).getUser(wrongEmail, wrongPassword)
        assertNull(result)
    }

    @Test
    fun `When call to existsUserName, the value returned must be the correct value`() = runTest {
        // Given
        val user = createUser()
        whenever(userLocalDataSource.existsUserName(user.userName)).thenReturn(true)

        // When
        val result = userRepository.existsUserName(user.userName)

        // Then
        verify(userLocalDataSource).existsUserName(user.userName)
        assertTrue(result)
    }

    @Test
    fun `When call to existsEmail, the value returned must be the correct value`() = runTest {
        // Given
        val user = createUser()
        whenever(userLocalDataSource.existsEmail(user.email)).thenReturn(false)

        // When
        val result = userRepository.existsEmail(user.email)

        // Then
        verify(userLocalDataSource).existsEmail(user.email)
        assertFalse(result)
    }

    @Test
    fun `When call to insertUser, the object User is passed to the DataSource`() = runTest {
        // Given
        val user = createUser()

        // When
        userRepository.insertUser(user)

        // Then
        val captorUser = argumentCaptor<User>()
        verify(userLocalDataSource).insertUser(captorUser.capture())

        assertEquals(user.userName, captorUser.firstValue.userName)
        assertEquals(user.firstName, captorUser.firstValue.firstName)
        assertEquals(user.lastName, captorUser.firstValue.lastName)
        assertEquals(user.email, captorUser.firstValue.email)
        assertEquals(user.password, captorUser.firstValue.password)
    }

    @Test
    fun `When SQLiteException is thrown, insertUser evolve with AppError Database`() = runTest {
        // Given
        val user = createUser()

        doAnswer { throw SQLiteException() }
            .whenever(userLocalDataSource).insertUser(any())

        // When & Then
        assertFailsWith<AppError.Database> {
            userRepository.insertUser(user)
        }
    }

    private fun createUser() =
        User(
            userName = "juanje",
            firstName = "Juan",
            lastName = "Bonito",
            email = "juanje@example.com",
            password = "password123"
        )
}