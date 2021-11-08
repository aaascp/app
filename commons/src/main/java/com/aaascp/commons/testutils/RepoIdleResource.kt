package com.aaascp.commons.testutils

import androidx.test.espresso.idling.CountingIdlingResource
import timber.log.Timber

object RepoIdleResource {

    private const val RESOURCE = "REPO_RESOURCE"

    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        try {
            countingIdlingResource.increment()
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }

    fun decrement() {
        try {
            countingIdlingResource.decrement()
        } catch (exception: Exception) {
            Timber.e(exception)
        }
    }
}
