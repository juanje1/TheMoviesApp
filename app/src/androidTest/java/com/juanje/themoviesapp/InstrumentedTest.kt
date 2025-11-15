package com.juanje.themoviesapp

import android.content.Context
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.juanje.themoviesapp.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoilApi::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class InstrumentedTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val id: Int = (0..100000).random()
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun navigationToMovieDetail() {
        // Check login fields
        checkLoginFields()

        // Navigate to register
        composeTestRule.onNodeWithTag(context.getString(R.string.login_register_test), useUnmergedTree = true).performClick()

        // Check register fields
        checkRegisterFields()

        // Fill register fields
        fillRegisterFields()

        // Register action and navigate to login
        composeTestRule.onNodeWithTag(context.getString(R.string.register_register_test), useUnmergedTree = true).performClick()

        // Check login fields
        checkLoginFields()

        // Fill login fields
        composeTestRule.onNodeWithTag(context.getString(R.string.login_email_test), useUnmergedTree = true)
            .performTextInput(context.getString(R.string.register_example_email_init_test)+"_$id@"+
                context.getString(R.string.register_example_email_end_test))

        composeTestRule.onNodeWithTag(context.getString(R.string.login_password_test), useUnmergedTree = true)
            .performTextInput(context.getString(R.string.register_example_password_test)+"_$id")

        // Navigate to home
        composeTestRule.onNodeWithTag(context.getString(R.string.login_login_test), useUnmergedTree = true).performClick()

        // Check home is not empty
        val timeoutMillis = TimeUnit.SECONDS.toMillis(10)

        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllNodesWithTag(context.getString(R.string.home_movie_list_test))
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Check home fields
        checkHomeFields()

        // Navigate to movie detail
        val movieId: Int = (0..5).random()

        composeTestRule.onNodeWithTag(context.getString(R.string.home_movie_list_test))
            .onChildAt(movieId)
            .performClick()

        // Check detail fields
        checkDetailFields()
    }

    private fun checkLoginFields() {
        composeTestRule.onNodeWithTag(context.getString(R.string.login_email_test), useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.login_password_test), useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.login_login_test), useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.login_register_test), useUnmergedTree = true).assertExists()
    }

    private fun checkRegisterFields() {
        composeTestRule.onNodeWithTag(context.getString(R.string.register_user_name_test), useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.register_first_name_test), useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.register_last_name_test), useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.register_email_test), useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.register_password_test), useUnmergedTree = true).assertExists()
    }

    private fun fillRegisterFields() {
        composeTestRule.onNodeWithTag(context.getString(R.string.register_user_name_test), useUnmergedTree = true)
            .performTextInput(context.getString(R.string.register_example_user_name_test)+"_$id")

        composeTestRule.onNodeWithTag(context.getString(R.string.register_first_name_test), useUnmergedTree = true)
            .performTextInput(context.getString(R.string.register_example_user_name_test)+"_$id")

        composeTestRule.onNodeWithTag(context.getString(R.string.register_last_name_test), useUnmergedTree = true)
            .performTextInput(context.getString(R.string.register_example_first_name_test)+"_$id")

        composeTestRule.onNodeWithTag(context.getString(R.string.register_email_test), useUnmergedTree = true)
            .performTextInput(context.getString(R.string.register_example_email_init_test)+"_$id@"+
                context.getString(R.string.register_example_email_end_test))

        composeTestRule.onNodeWithTag(context.getString(R.string.register_password_test), useUnmergedTree = true)
            .performTextInput(context.getString(R.string.register_example_password_test)+"_$id")
    }

    private fun checkHomeFields() {
        composeTestRule.onNodeWithTag(context.getString(R.string.home_movie_list_test)).assertExists()
    }

    private fun checkDetailFields() {
        composeTestRule.onNodeWithTag(context.getString(R.string.detail_movie_image_test)).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.detail_movie_title_test)).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.detail_movie_overview_test)).assertExists()
        composeTestRule.onNodeWithTag(context.getString(R.string.detail_movie_favourite_test)).assertExists()
    }
}