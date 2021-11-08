package com.aaascp.repo.presentation

import android.content.pm.ActivityInfo
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.paging.ExperimentalPagingApi
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import com.aaascp.commons.R.style.FragmentScenarioEmptyFragmentActivityTheme
import com.aaascp.commons.testutils.launchFragmentInHiltContainer
import com.aaascp.repo.R
import com.aaascp.repo.data.RepoRepository
import com.aaascp.repo.di.RepositoryModule
import com.aaascp.repo.di.TestRepoRepository
import com.aaascp.repo.domain.Repo
import com.aaascp.repo.factories.createRandomRepo
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@InternalCoroutinesApi
@HiltAndroidTest
@LargeTest
@UninstallModules(RepositoryModule::class)
class RepoFragmentUiTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    lateinit var repoRepository: RepoRepository

    private lateinit var navController: TestNavHostController
    private val repoFeature: Repo = createRandomRepo()

    @Before
    fun setUp() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        repoRepository = TestRepoRepository(listOf(repoFeature))
        hiltRule.inject()
    }

    @Test
    fun withSingleItem_whenOrientationIsPortrait_shouldBindValuesCorrectly() {
        launchFragment(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        onView(withId(R.id.cardStars)).check(matches(withText(repoFeature.stars.toString())))
        onView(withId(R.id.cardAuthor)).check(matches(withText(repoFeature.owner.name)))
        onView(withId(R.id.cardTitle)).check(matches(withText(repoFeature.name)))

        onView(withId(R.id.messageText)).check(matches(not(isDisplayed())))
        onView(withId(R.id.loadingProgressBar)).check(matches(not(isDisplayed())))
        onView(withId(R.id.actionButton)).check(matches(not(isDisplayed())))
    }

    @Test
    fun onItemClick_whenOrientationEqualsLandscape_shouldBindDetailsCorrectly() {
        launchFragment(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        onView(withId(R.id.rootLayout))
            .check(matches(isCompletelyDisplayed()))
            .perform(click())

        onView(withId(R.id.detailsTitle)).check(matches(withText(repoFeature.name)))
        onView(withId(R.id.detailsAuthor)).check(matches(withText(repoFeature.owner.name)))
        onView(withId(R.id.detailsStars)).check(matches(withText(repoFeature.stars.toString())))
        onView(withId(R.id.detailsForks)).check(matches(withText(repoFeature.forks.toString())))
        onView(withId(R.id.detailsDescription)).check(matches(withText(repoFeature.description)))

        onView(withId(R.id.detailsEmpty)).check(matches(not(isDisplayed())))
    }

    @Test
    fun onItemClick_whenOrientationIsLandscape_shouldNotNavigate() {
        launchFragment(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        onView(withId(R.id.rootLayout))
            .check(matches(isCompletelyDisplayed()))
            .perform(click())

        assertNotEquals(
            R.id.RepoDetailFragment,
            navController.currentDestination!!.id
        )
    }

    @Test
    fun onItemClick_whenOrientationEqualsPortrait_shouldNavigateToItemDetails() {
        launchFragment(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

        onView(withId(R.id.rootLayout))
            .check(matches(isCompletelyDisplayed()))
            .perform(click())

        assertEquals(
            R.id.RepoDetailFragment,
            navController.currentDestination!!.id
        )
    }

    private fun launchFragment(orientation: Int) {
        launchFragmentInHiltContainer<RepoFragment>(
            orientation,
            themeResId = FragmentScenarioEmptyFragmentActivityTheme
        ) {
            navController.setGraph(R.navigation.repo_nav_graph)
            Navigation.setViewNavController(requireView(), navController)
        }
    }
}
