package com.aaascp.repo.presentation

import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import com.aaascp.commons.testutils.launchFragmentInHiltContainer
import com.aaascp.commons.testutils.RepoIdleResource
import com.aaascp.repo.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@InternalCoroutinesApi
@HiltAndroidTest
@LargeTest
class RepoFragmentE2ETest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        IdlingRegistry.getInstance().register(RepoIdleResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(RepoIdleResource.countingIdlingResource)
    }

    @Test
    fun onItemClick_whenOrientationEqualsLandscape_shouldNavigateToItemDetails() {
        launchFragmentInHiltContainer<RepoFragment>()

        onView(withId(R.id.repoListRecyclerView))
            .check(matches(isCompletelyDisplayed()))
            .check(isNotEmpty())
    }
}

private fun isNotEmpty(): ViewAssertion {
    return ViewAssertion { view, noViewFoundException ->
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        assertFalse((view as RecyclerView).adapter!!.itemCount == 0)
    }
}
