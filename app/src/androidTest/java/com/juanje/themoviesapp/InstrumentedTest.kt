package com.juanje.themoviesapp

import android.content.Context
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.juanje.themoviesapp.ui.MainActivity
import com.juanje.themoviesapp.ui.screens.detail.DetailViewModel
import com.juanje.themoviesapp.ui.screens.home.HomeViewModel
import com.juanje.themoviesapp.utils.CheckDetailRobot
import com.juanje.themoviesapp.utils.CheckHomeRobot
import com.juanje.themoviesapp.utils.IdlingResourceProvider
import com.juanje.themoviesapp.utils.CheckLoginRobot
import com.juanje.themoviesapp.utils.CheckRegisterRobot
import com.juanje.themoviesapp.utils.FillLoginRobot
import com.juanje.themoviesapp.utils.FillRegisterRobot
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

    private val id: Int = (0..100000).random()
    private val movieId: Int = (1..6).random()
    private val timeoutMillis = TimeUnit.SECONDS.toMillis(20)
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        homeViewModel = ViewModelProvider(composeTestRule.activity)[HomeViewModel::class.java]
        detailViewModel = ViewModelProvider(composeTestRule.activity)[DetailViewModel::class.java]

        IdlingRegistry.getInstance().register(IdlingResourceProvider.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(IdlingResourceProvider.countingIdlingResource)
    }

    @Test
    fun navigationToMovieDetail() {
        // 1. Login -> Register
        checkLoginFields()
        composeTestRule.onTag(context.getString(R.string.login_register_test)).performClick()
        composeTestRule.waitForIdle()

        // 2. Register
        checkRegisterFields()
        fillRegisterFields()
        composeTestRule.onTag(context.getString(R.string.register_register_test)).performScrollTo()
        composeTestRule.waitForIdle()
        composeTestRule.onTag(context.getString(R.string.register_register_test)).performClick()
        composeTestRule.waitForIdle()

        // 3. Login
        checkLoginFields()
        fillLoginFields()
        composeTestRule.onTag(context.getString(R.string.login_login_test)).performClick()
        composeTestRule.waitForIdle()

        // 4. Home
        if (!checkHomeFields(movieId)) return

        // 5. Home -> Detail
        composeTestRule.onTag(context.getString(R.string.home_movie_list_test)+"_$movieId").performScrollTo()
        composeTestRule.waitForIdle()
        composeTestRule.onTag(context.getString(R.string.home_movie_list_test)+"_$movieId").performClick()
        composeTestRule.waitForIdle()

        // 6. Detail
        checkDetailFields(movieId)
    }

    private fun checkLoginFields() {
        val tags = CheckLoginRobot.getFields(context)

        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllTags(tags.first()).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.waitForIdle()

        tags.forEach { tag ->
            composeTestRule.onTag(tag).scrollToAndAssertDisplayed(composeTestRule)
        }
    }

    private fun checkRegisterFields() {
        val tags = CheckRegisterRobot.getFields(context)

        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllTags(tags.first()).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.waitForIdle()

        tags.forEach { tag ->
            composeTestRule.onTag(tag).scrollToAndAssertDisplayed(composeTestRule)
        }
    }

    private fun fillLoginFields() {
        val tags = FillLoginRobot.getFields(context, id)

        tags.forEach { (tag, value) ->
            composeTestRule.onTag(tag).scrollToAndType(composeTestRule, value)
        }

        composeTestRule.onTag(context.getString(R.string.login_password_test)).performImeAction()
        composeTestRule.waitForIdle()
    }

    private fun fillRegisterFields() {
        val tags = FillRegisterRobot.getFields(context, id)

        tags.forEach { (tag, value) ->
            composeTestRule.onTag(tag).scrollToAndType(composeTestRule, value)
        }

        composeTestRule.onTag(context.getString(R.string.register_password_test)).performImeAction()
        composeTestRule.waitForIdle()
    }

    private fun checkHomeFields(movieId: Int): Boolean {
        val loadingSpinner = context.getString(R.string.home_loading_spinner_text)
        val movieListHome = context.getString(R.string.home_movie_list_test)
        val snackBarHost = context.getString(R.string.snack_bar_host_test)
        val tags = CheckHomeRobot.getFields(context, movieId)

        var hasErrorEncountered = false

        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllTags(loadingSpinner).fetchSemanticsNodes().isEmpty()
        }
        composeTestRule.waitForIdle()

        composeTestRule.waitUntil(timeoutMillis) {
            val homeLoaded = composeTestRule.onAllTags(movieListHome).fetchSemanticsNodes().isNotEmpty()
            val hasError = composeTestRule.onTag(snackBarHost).onChildren().fetchSemanticsNodes().isNotEmpty()

            hasErrorEncountered = hasError
            homeLoaded || hasError
        }
        composeTestRule.waitForIdle()

        if (hasErrorEncountered) {
            composeTestRule.onTag(snackBarHost).assertIsDisplayed()
            return false
        }

        composeTestRule.onTag(movieListHome).assertIsDisplayed()
        composeTestRule.waitForIdle()

        tags.forEach { tag -> waitAndAssertItem(tag) }
        return true
    }

    private fun checkDetailFields(movieId: Int) {
        val snackBarHost = context.getString(R.string.snack_bar_host_test)
        val tags = CheckDetailRobot.getFields(context, movieId)

        var hasErrorEncountered = false

        composeTestRule.waitUntil(timeoutMillis) {
            val detailLoaded = composeTestRule.onAllTags(tags.first()).fetchSemanticsNodes().isNotEmpty()
            val hasError = composeTestRule.onTag(snackBarHost).onChildren().fetchSemanticsNodes().isNotEmpty()

            hasErrorEncountered = hasError
            detailLoaded || hasError
        }
        composeTestRule.waitForIdle()

        if (hasErrorEncountered) {
            composeTestRule.onTag(snackBarHost).assertIsDisplayed()
            return
        }

        tags.forEach { tag ->
            composeTestRule.onTag(tag).scrollToAndAssertDisplayed(composeTestRule)
        }
    }

    private fun SemanticsNodeInteraction.scrollToAndAssertDisplayed(
        composeTestRule: ComposeContentTestRule
    ) {
        this.performScrollTo()
        composeTestRule.waitForIdle()

        this.assertIsDisplayed()
        composeTestRule.waitForIdle()
    }

    private fun SemanticsNodeInteraction.scrollToAndType(
        composeTestRule: ComposeContentTestRule, text: String
    ) {
        this.performScrollTo()
        composeTestRule.waitForIdle()

        this.performClick()
        composeTestRule.waitForIdle()

        this.performTextInput(text)
        composeTestRule.waitForIdle()
    }

    private fun waitAndAssertItem(tag: String) {
        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllTags(tag).fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.waitForIdle()

        composeTestRule.onTag(tag).assertIsDisplayed()
        composeTestRule.waitForIdle()
    }
}

fun SemanticsNodeInteractionsProvider.onTag(tag: String) =
    onNodeWithTag(tag, useUnmergedTree = true)

fun SemanticsNodeInteractionsProvider.onAllTags(tag: String) =
    onAllNodesWithTag(tag, useUnmergedTree = true)