package com.aaascp.repo.presentation

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates

fun createLoadState(
    isEmpty: Boolean = false,
    isLoading: Boolean = false,
    hasLoaded: Boolean = false,
    hasError: Boolean = false
): CombinedLoadStates {
    val refreshState = when {
        isEmpty -> LoadState.NotLoading(true)
        else -> LoadState.Error(Throwable())
    }

    val mediatorRefreshState = when {
        isLoading -> LoadState.Loading
        hasLoaded -> LoadState.NotLoading(true)
        hasError -> LoadState.Error(Throwable())
        else -> LoadState.Error(Throwable())
    }

    val sourceRefreshState = when {
        hasLoaded -> LoadState.NotLoading(true)
        else -> LoadState.Error(Throwable())
    }

    return CombinedLoadStates(
        refresh = refreshState,
        mediator = LoadStates(
            refresh = mediatorRefreshState,
            prepend = LoadState.Loading,
            append = LoadState.Loading
        ),
        source = LoadStates(
            refresh = sourceRefreshState,
            prepend = LoadState.Loading,
            append = LoadState.Loading
        ),
        prepend = LoadState.Loading,
        append = LoadState.Loading
    )
}
