package com.juanje.themoviesapp

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.juanje.themoviesapp.idling.CombinedLoadingIdlingResource
import com.juanje.themoviesapp.ui.MainActivity
import com.juanje.themoviesapp.ui.screens.detail.DetailViewModel
import com.juanje.themoviesapp.ui.screens.home.HomeViewModel
import com.juanje.themoviesapp.utils.IdlingResourceProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
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

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var combinedLoadingIdlingResource: CombinedLoadingIdlingResource

    private val id: Int = (0..100000).random()
    private val movieId: Int = (1..6).random()
    private val timeoutMillis = TimeUnit.SECONDS.toMillis(10)
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        homeViewModel = ViewModelProvider(composeTestRule.activity)[HomeViewModel::class.java]
        detailViewModel = ViewModelProvider(composeTestRule.activity)[DetailViewModel::class.java]
        combinedLoadingIdlingResource = CombinedLoadingIdlingResource(
            loadingCheckers = listOf(homeViewModel.isImageLoading, detailViewModel.isImageLoading)
        )

        composeTestRule.registerIdlingResource(combinedLoadingIdlingResource)
        IdlingRegistry.getInstance().register(IdlingResourceProvider.countingIdlingResource)
    }

    @After
    fun tearDown() {
        composeTestRule.unregisterIdlingResource(combinedLoadingIdlingResource)
        IdlingRegistry.getInstance().unregister(IdlingResourceProvider.countingIdlingResource)
    }

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
        val movieListHome = context.getString(R.string.home_movie_list_test)
        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllNodesWithTag(movieListHome)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }

        // Check home fields
        checkHomeFields(movieId)

        // Navigate to movie detail
        composeTestRule.onNodeWithTag(context.getString(R.string.home_movie_list_test)+"_$movieId").performClick()

        // Check detail fields
        checkDetailFields(movieId)
    }

    private fun checkLoginFields() {
        composeTestRule.onNodeWithTag(context.getString(R.string.login_email_test), useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.login_password_test), useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.login_login_test), useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.login_register_test), useUnmergedTree = true).assertIsDisplayed()
    }

    private fun checkRegisterFields() {
        composeTestRule.onNodeWithTag(context.getString(R.string.register_user_name_test), useUnmergedTree = true).performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.register_first_name_test), useUnmergedTree = true).performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.register_last_name_test), useUnmergedTree = true).performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.register_email_test), useUnmergedTree = true).performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.register_password_test), useUnmergedTree = true).performScrollTo().assertIsDisplayed()
    }

    private fun fillRegisterFields() {
        composeTestRule.onNodeWithTag(context.getString(R.string.register_user_name_test), useUnmergedTree = true)
            .performScrollTo().performTextInput(context.getString(R.string.register_example_user_name_test)+"_$id")

        composeTestRule.onNodeWithTag(context.getString(R.string.register_first_name_test), useUnmergedTree = true)
            .performScrollTo().performTextInput(context.getString(R.string.register_example_user_name_test)+"_$id")

        composeTestRule.onNodeWithTag(context.getString(R.string.register_last_name_test), useUnmergedTree = true)
            .performScrollTo().performTextInput(context.getString(R.string.register_example_first_name_test)+"_$id")

        composeTestRule.onNodeWithTag(context.getString(R.string.register_email_test), useUnmergedTree = true)
            .performScrollTo().performTextInput(context.getString(R.string.register_example_email_init_test)+"_$id@"+
                context.getString(R.string.register_example_email_end_test))

        composeTestRule.onNodeWithTag(context.getString(R.string.register_password_test), useUnmergedTree = true)
            .performScrollTo().performTextInput(context.getString(R.string.register_example_password_test)+"_$id")
    }

    private fun checkHomeFields(movieId: Int) {
        composeTestRule.onNodeWithTag(context.getString(R.string.home_movie_list_test)).assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.home_movie_image_test)+"_$movieId", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.home_movie_favourite_test)+"_$movieId", useUnmergedTree = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.home_movie_title_test)+"_$movieId", useUnmergedTree = true).assertIsDisplayed()
    }

    private fun checkDetailFields(movieId: Int) {
        composeTestRule.onNodeWithTag(context.getString(R.string.detail_movie_image_test)+"_$movieId").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.detail_movie_title_test)+"_$movieId").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.detail_movie_overview_test)+"_$movieId").performScrollTo().assertIsDisplayed()
        composeTestRule.onNodeWithTag(context.getString(R.string.detail_movie_favourite_test)+"_$movieId").performScrollTo().assertIsDisplayed()
    }
}