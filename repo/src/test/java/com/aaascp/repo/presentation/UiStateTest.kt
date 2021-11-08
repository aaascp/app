package com.aaascp.repo.presentation

import androidx.paging.PagingData
import org.junit.Assert.assertEquals
import org.junit.Test

class UiStateTest {

    @Test
    fun emptyUiState_shouldShowOnlyEmptyState() {
        assertState(UiState.Empty, isEmptyVisible = true)
    }

    @Test
    fun loadingUiState_shouldShowOnlyLoadingState() {
        assertState(UiState.Loading, isLoadingVisible = true)
    }

    @Test
    fun loadedUiState_shouldShowOnlyLoadedState() {
        assertState(UiState.Loaded(PagingData.empty()), isListVisible = true)
    }

    @Test
    fun loadErrorUiState_shouldShowOnlyErrorState() {
        assertState(UiState.LoadError, isErrorVisible = true)
    }

    @Test
    fun invalidUiState_shouldShowOnlyErrorState() {
        assertState(UiState.Invalid, isErrorVisible = true)
    }

    private fun assertState(
        uiState: UiState,
        isEmptyVisible: Boolean = false,
        isLoadingVisible: Boolean = false,
        isListVisible: Boolean = false,
        isErrorVisible: Boolean = false
    ) {
        assertEquals(isEmptyVisible, uiState.isEmptyVisible)
        assertEquals(isLoadingVisible, uiState.isLoadingVisible)
        assertEquals(isListVisible, uiState.isListVisible)
        assertEquals(isErrorVisible, uiState.isErrorVisible)
    }
}
