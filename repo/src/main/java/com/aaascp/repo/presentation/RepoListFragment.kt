package com.aaascp.repo.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aaascp.commons.MarginItemDecoration
import com.aaascp.commons.testutils.RepoIdleResource
import com.aaascp.commons.getOrientation
import com.aaascp.repo.R
import com.aaascp.repo.databinding.FragmentRepoListBinding
import com.aaascp.repo.domain.Repo
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class RepoListFragment : Fragment(), OnRepoSelectedListener {

    private var _binding: FragmentRepoListBinding? = null
    private val binding get() = _binding!!

    private val repoListViewModel: RepoListViewModel by activityViewModels()
    private val repoListAdapter by lazy { RepoListAdapter(this) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoListBinding.inflate(inflater, container, false)

        binding.actionButton.setOnClickListener { repoListAdapter.retry() }

        with(binding.repoListRecyclerView) {
            adapter = repoListAdapter
            addItemDecoration(getListItemDecoration())
        }
        return binding.root
    }

    private fun getListItemDecoration() =
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        } else {
            MarginItemDecoration(
                resources.getDimension(R.dimen.margin_vertical_default).toInt()
            )
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RepoIdleResource.increment()
        repoListViewModel.onViewCreated()

        repoListAdapter.addLoadStateListener { loadState ->
            repoListViewModel.onReposLoaded(loadState, repoListAdapter.itemCount == 0)
        }

        repoListViewModel.uiState.observe(this, ::handleStateChange)
        repoListViewModel.generalError.observe(this, ::handleGeneralError)
    }

    private fun handleStateChange(newState: UiState) {
        binding.messageText.isVisible = newState.isEmptyVisible
        binding.loadingProgressBar.isVisible = newState.isLoadingVisible
        binding.repoListRecyclerView.isVisible = newState.isListVisible
        binding.actionButton.isVisible = newState.isErrorVisible

        if (newState is UiState.Loaded) {
            handleReposLoaded(newState.data)
        }
    }

    private fun handleReposLoaded(repos: PagingData<Repo>) {
        lifecycleScope.launch {
            repoListAdapter.submitData(repos)
            RepoIdleResource.decrement()
        }
    }

    private fun handleGeneralError(error: GeneralError) {
        Toast.makeText(
            requireContext(),
            error.message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onItemSelected(item: Repo) {
        repoListViewModel.onItemSelected(
            item,
            requireActivity().getOrientation()
        )
    }
}

interface OnRepoSelectedListener {
    fun onItemSelected(item: Repo)
}

