package com.juanje.data.common

import android.database.sqlite.SQLiteException
import com.juanje.domain.common.AppError
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertFailsWith

@RunWith(RobolectricTestRunner::class)
class SafeCallTest {

    @Test
    fun `When call is successful, should return value`() = runTest {
        // Given
        val expected = "Success"

        // When
        val result = safeCall { expected }

        // Then
        assertEquals(expected, result)
    }

    @Test
    fun `When UnknownHostException occurs, should throw AppError Network`() = runTest {
        // When & Then
        assertFailsWith<AppError.Network> {
            safeCall { throw UnknownHostException() }
        }
    }

    @Test
    fun `When SQLiteException occurs, should throw AppError Database`() = runTest {
        // When & Then
        assertFailsWith<AppError.Database> {
            safeCall { throw SQLiteException() }
        }
    }

    @Test
    fun `When CancellationException occurs, should rethrow it without mapping`() = runTest {
        // When & Then
        assertFailsWith<CancellationException> {
            safeCall { throw CancellationException() }
        }
    }

    @Test
    fun `When unexpected error occurs, should throw AppError Unexpected`() = runTest {
        // Given
        val errorMessage = "Something went wrong"

        // When & Then
        val exception = assertFailsWith<AppError.Unexpected> {
            safeCall { throw RuntimeException(errorMessage) }
        }
        assertEquals(errorMessage, exception.message)
    }
}
