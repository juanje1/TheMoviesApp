package com.juanje.themoviesapp

import android.content.Context
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.annotation.ExperimentalCoilApi
import com.juanje.themoviesapp.ui.MainActivity
import com.juanje.themoviesapp.utils.CheckDetailRobot
import com.juanje.themoviesapp.utils.CheckHomeRobot
import com.juanje.themoviesapp.utils.CheckLoginRobot
import com.juanje.themoviesapp.utils.CheckRegisterRobot
import com.juanje.themoviesapp.utils.FillLoginRobot
import com.juanje.themoviesapp.utils.FillRegisterRobot
import com.juanje.themoviesapp.utils.IdlingResourceProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assume.assumeTrue
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

    private val userId: Int = (0..100000).random()
    private val timeoutMillis = TimeUnit.SECONDS.toMillis(20)
    private val context: Context = ApplicationProvider.getApplicationContext()

    private lateinit var businessId: String

    @Before
    fun setUp() {
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

        // 2. Register
        checkRegisterFields()
        fillRegisterFields()
        composeTestRule.onTag(context.getString(R.string.register_register_test)).performScrollTo()
        composeTestRule.onTag(context.getString(R.string.register_register_test)).performClick()

        // 3. Login
        checkLoginFields()
        fillLoginFields()
        composeTestRule.onTag(context.getString(R.string.login_login_test)).performClick()

        // 4. Home
        val movieTitle = checkHomeFields()

        // 5. Home -> Detail
        val homeMovieTag = context.getString(R.string.home_movie_list_test) + "_$businessId"
        composeTestRule.onTag(homeMovieTag).performScrollTo()
        composeTestRule.onTag(homeMovieTag).performClick()

        // 6. Detail
        checkDetailFields(businessId, movieTitle)
    }

    private fun checkLoginFields() {
        val tags = CheckLoginRobot.getFields(context)

        tags.forEach { tag ->
            composeTestRule.waitUntil(timeoutMillis) {
                composeTestRule.onAllTags(tag).fetchSemanticsNodes().isNotEmpty()
            }
            composeTestRule.onTag(tag).scrollToAndAssertDisplayed()
        }
    }

    private fun checkRegisterFields() {
        val tags = CheckRegisterRobot.getFields(context)

        tags.forEach { tag ->
            composeTestRule.waitUntil(timeoutMillis) {
                composeTestRule.onAllTags(tag).fetchSemanticsNodes().isNotEmpty()
            }
            composeTestRule.onTag(tag).scrollToAndAssertDisplayed()
        }
    }

    private fun fillLoginFields() {
        val tags = FillLoginRobot.getFields(context, userId)

        tags.forEach { (tag, value) ->
            composeTestRule.onTag(tag).scrollToAndType(value)
        }
        composeTestRule.onTag(context.getString(R.string.login_password_test)).performImeAction()
    }

    private fun fillRegisterFields() {
        val tags = FillRegisterRobot.getFields(context, userId)

        tags.forEach { (tag, value) ->
            composeTestRule.onTag(tag).scrollToAndType(value)
        }
        composeTestRule.onTag(context.getString(R.string.register_password_test)).performImeAction()
    }

    private fun checkHomeFields(): String {
        val loadingSpinner = context.getString(R.string.home_loading_spinner_text)
        val movieListHome = context.getString(R.string.home_movie_list_test)
        val snackBarHost = context.getString(R.string.snack_bar_host_test)

        composeTestRule.waitUntil(timeoutMillis) {
            composeTestRule.onAllTags(loadingSpinner).fetchSemanticsNodes().isEmpty()
        }

        composeTestRule.waitUntil(timeoutMillis) {
            val homeLoaded = composeTestRule.onAllTags(movieListHome).fetchSemanticsNodes().isNotEmpty()
            val hasError = composeTestRule.onAllTags(snackBarHost).onFirst().onChildren().fetchSemanticsNodes().isNotEmpty()

            setError(hasError, snackBarHost)
            homeLoaded
        }
        composeTestRule.onTag(movieListHome).assertIsDisplayed()

        val movieTag = getHomeMovieTag()
        if (movieTag.isEmpty()) error(context.getString(R.string.error_movie_list_test))
        businessId = movieTag.substringAfter(context.getString(R.string.home_movie_list_test)+"_")

        val tags = CheckHomeRobot.getFields(context, businessId)
        tags.forEach { tag -> waitScrollToAndAssertDisplayed(tag) }

        val titleTag = context.getString(R.string.home_movie_title_test)+"_$businessId"
        val movieTitle = composeTestRule.onTag(titleTag).fetchSemanticsNode().config
            .getOrNull(SemanticsProperties.Text)?.firstOrNull()?.text ?: ""

        return movieTitle
    }

    private fun checkDetailFields(businessId: String, movieTitle: String) {
        val tags = CheckDetailRobot.getFields(context, businessId)
        tags.forEach { tag -> waitScrollToAndAssertDisplayed(tag) }

        composeTestRule.onTag(context.getString(R.string.detail_movie_title_test)+"_${businessId}")
            .performScrollTo().assertTextEquals(movieTitle)
    }

    private fun getHomeMovieTag(): String {
        val snackBarHost = context.getString(R.string.snack_bar_host_test)
        val prefix = context.getString(R.string.home_movie_list_test)+"_"
        val description = context.getString(R.string.description_matcher_movie_test)
        val movieMatcher = SemanticsMatcher(description) { node ->
            node.config.getOrNull(SemanticsProperties.TestTag)?.startsWith(prefix) == true
        }

        composeTestRule.waitUntil(timeoutMillis) {
            val movieLoaded = composeTestRule.onNodes(movieMatcher).fetchSemanticsNodes().isNotEmpty()
            val hasError = composeTestRule.onAllTags(snackBarHost).onFirst().onChildren().fetchSemanticsNodes().isNotEmpty()

            setError(hasError, snackBarHost)
            movieLoaded
        }

        val movieNode = composeTestRule.onNodes(movieMatcher).onFirst()
        return movieNode.fetchSemanticsNode().config.getOrNull(SemanticsProperties.TestTag) ?: ""
    }

    private fun waitScrollToAndAssertDisplayed(tag: String) {
        val snackBarHost = context.getString(R.string.snack_bar_host_test)

        composeTestRule.waitUntil(timeoutMillis) {
            val itemLoaded = composeTestRule.onAllTags(tag).fetchSemanticsNodes().isNotEmpty()
            val hasError = composeTestRule.onAllTags(snackBarHost).onFirst().onChildren().fetchSemanticsNodes().isNotEmpty()

            setError(hasError, snackBarHost)
            itemLoaded
        }

        composeTestRule.onTag(tag).scrollToAndAssertDisplayed()
    }

    private fun setError(hasError: Boolean, snackBarHost: String) {
        if (hasError) {
            composeTestRule.onTag(snackBarHost).assertIsDisplayed()
            assumeTrue(context.getString(R.string.assume_internet_test), false)
        }
    }

    private fun SemanticsNodeInteraction.scrollToAndAssertDisplayed() = apply {
        this.performScrollTo()
        this.assertIsDisplayed()
    }

    private fun SemanticsNodeInteraction.scrollToAndType(text: String) = apply {
        this.performScrollTo()
        this.performClick()
        this.performTextInput(text)
    }
}

fun SemanticsNodeInteractionsProvider.onTag(tag: String) =
    onNodeWithTag(tag, useUnmergedTree = true)

fun SemanticsNodeInteractionsProvider.onAllTags(tag: String) =
    onAllNodesWithTag(tag, useUnmergedTree = true)

fun SemanticsNodeInteractionsProvider.onNodes(matcher: SemanticsMatcher) =
    onAllNodes(matcher, useUnmergedTree = true)