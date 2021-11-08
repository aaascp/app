package com.aaascp.repo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aaascp.commons.Orientation
import com.aaascp.repo.domain.Repo
import com.aaascp.repo.usecases.GetAllKotlinReposOrderedByStarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val getAllKotlinReposOrderedByStarsUseCase: GetAllKotlinReposOrderedByStarsUseCase
) : ViewModel() {

    private val _selectedRepo = MutableLiveData<RepoSelectedState>()
    val selectedRepo: LiveData<RepoSelectedState>
        get() = _selectedRepo

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState>
        get() = _uiState

    private val _generalError = MutableLiveData<GeneralError>()
    val generalError: LiveData<GeneralError>
        get() = _generalError

    init {
        viewModelScope.launch {
            getAllKotlinReposOrderedByStarsUseCase
                .execute()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _uiState.value = UiState.Loaded(it)
                }
        }
    }

    fun onReposLoaded(loadState: CombinedLoadStates, listIsEmpty: Boolean) {
        if (_uiState.value is UiState.Loaded) {
            return
        }

        val isEmpty =
            loadState.refresh is LoadState.NotLoading && listIsEmpty
        val isLoading = loadState.mediator?.refresh is LoadState.Loading
        val hasLoadError = loadState.mediator?.refresh is LoadState.Error && listIsEmpty

        _uiState.value = when {
            isEmpty -> UiState.Empty
            isLoading -> UiState.Loading
            hasLoadError -> UiState.LoadError
            else -> UiState.Invalid
        }

        val errorState = getAnyError(loadState)
        if (errorState != null) {
            _generalError.value = GeneralError(errorState.error.message)
        }
    }

    fun onItemSelected(item: Repo, orientation: Orientation) {
        _selectedRepo.value = RepoSelectedState.Selected(item, orientation)
    }

    fun onRepoSelectedHandled(item: Repo, orientation: Orientation) {
        _selectedRepo.value = RepoSelectedState.SelectedHandled(item, orientation)
    }

    fun onViewCreated() {
        _selectedRepo.value = RepoSelectedState.NoneSelected
    }
}

sealed class UiState(
    val isEmptyVisible: Boolean = false,
    val isLoadingVisible: Boolean = false,
    val isListVisible: Boolean = false,
    val isErrorVisible: Boolean = false
) {
    object Empty : UiState(isEmptyVisible = true)
    object Loading : UiState(isLoadingVisible = true)
    class Loaded(val data: PagingData<Repo>) : UiState(isListVisible = true)
    object LoadError : UiState(isErrorVisible = true)
    object Invalid : UiState(isErrorVisible = true) {
        init {
            Timber.e(Exception("Invalid UiState"))
        }
    }
}

sealed class RepoSelectedState(
    val isEmptyVisible: Boolean = false,
    val isDetailsVisible: Boolean = false,
    val orientation: Orientation = Orientation.PORTRAIT
) {
    object NoneSelected : RepoSelectedState(isEmptyVisible = true)

    class Selected(val repo: Repo, orientation: Orientation) :
        RepoSelectedState(isDetailsVisible = true, orientation = orientation)

    class SelectedHandled(val repo: Repo, orientation: Orientation) :
        RepoSelectedState(isDetailsVisible = true, orientation = orientation)
}

data class GeneralError(val message: String?) : UiState(isErrorVisible = true)

private fun getAnyError(loadState: CombinedLoadStates): LoadState.Error? {
    return loadState.source.append as? LoadState.Error
        ?: loadState.source.prepend as? LoadState.Error
        ?: loadState.append as? LoadState.Error
        ?: loadState.prepend as? LoadState.Error
}
