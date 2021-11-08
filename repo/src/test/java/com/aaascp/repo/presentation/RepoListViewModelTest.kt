package com.aaascp.repo.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.aaascp.repo.factories.createRandomRepo
import com.aaascp.repo.usecases.GetAllKotlinReposOrderedByStarsUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsInstanceOf
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class RepoListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var getAllKotlinReposOrderedByStarsUseCase: GetAllKotlinReposOrderedByStarsUseCase

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    private fun getTestSubject(hasLoaded: Boolean = false): RepoListViewModel {
        val items = if (hasLoaded) {
            PagingData.from(listOf(createRandomRepo()))
        } else {
            PagingData.empty()
        }

        coEvery { getAllKotlinReposOrderedByStarsUseCase.execute() } returns flowOf(items)

        return RepoListViewModel(getAllKotlinReposOrderedByStarsUseCase)
    }

    @Test
    fun onReposLoaded_whenIsEmptyState_andListIsEmpty_shouldReturnUiStateEmpty() {
        val viewModel = getTestSubject()

        viewModel.onReposLoaded(createLoadState(isEmpty = true), listIsEmpty = true)

        val result = viewModel.uiState.value

        assertThat(
            result,
            IsInstanceOf(UiState.Empty::class.java)
        )

    }

    @Test
    fun onReposLoaded_whenIsEmptyState_andListIsNotEmpty_shouldNotReturnUiStateEmpty() {
        val viewModel = getTestSubject()

        viewModel.onReposLoaded(createLoadState(isEmpty = true), listIsEmpty = false)

        val result = viewModel.uiState.value

        assertFalse(result is UiState.Empty)

    }

    @Test
    fun onReposLoaded_whenIsLoadingState_shouldReturnUiStateLoading() {
        val viewModel = getTestSubject()

        viewModel.onReposLoaded(createLoadState(isLoading = true), listIsEmpty = true)

        val result = viewModel.uiState.value

        assertThat(
            result,
            IsInstanceOf(UiState.Loading::class.java)
        )
    }

    @Test
    fun onReposLoaded_whenIsErrorState_andListIsEmpty_shouldReturnUiStateError() {
        val viewModel = getTestSubject()

        viewModel.onReposLoaded(createLoadState(hasError = true), listIsEmpty = true)

        val result = viewModel.uiState.value

        assertThat(
            result,
            IsInstanceOf(UiState.LoadError::class.java)
        )

    }

    @Test
    fun onReposLoaded_whenIsErrorState_andListIsNotEmpty_shouldNotReturnUiStateError() {
        val viewModel = getTestSubject()

        viewModel.onReposLoaded(createLoadState(hasError = true), listIsEmpty = false)

        val result = viewModel.uiState.value

        assertTrue(result is UiState.Invalid)
    }
}
