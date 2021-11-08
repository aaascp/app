package com.aaascp.repo.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.ExperimentalPagingApi
import com.aaascp.commons.getOrientation
import com.aaascp.commons.load
import com.aaascp.repo.databinding.FragmentRepoDetailsBinding
import com.aaascp.repo.domain.Repo

@ExperimentalPagingApi
class RepoDetailFragment : Fragment() {

    private var _binding: FragmentRepoDetailsBinding? = null
    private val binding get() = _binding!!

    private val repoListViewModel: RepoListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repoListViewModel.selectedRepo.observe(this, ::handleRepoSelected)
    }

    private fun handleRepoSelected(repoSelectedState: RepoSelectedState) {
        bindGroupVisibility(repoSelectedState)

        when (repoSelectedState) {
            RepoSelectedState.NoneSelected -> return
            is RepoSelectedState.Selected -> bindSelected(repoSelectedState)
            is RepoSelectedState.SelectedHandled -> bindRepo(repoSelectedState.repo)
        }
    }

    private fun bindSelected(repoSelectedState: RepoSelectedState.Selected) {
        bindRepo(repoSelectedState.repo)
        repoListViewModel.onRepoSelectedHandled(
            repoSelectedState.repo,
            requireActivity().getOrientation()
        )
    }

    private fun bindRepo(repo: Repo) {
        with(repo) {
            binding.detailsAvatar.load(owner.avatarUrl)
            binding.detailsTitle.text = name
            binding.detailsAuthor.text = owner.name
            binding.detailsStars.text = stars.toString()
            binding.detailsForks.text = forks.toString()
            binding.detailsDescription.text = description
        }
    }

    private fun bindGroupVisibility(state: RepoSelectedState) {
        binding.detailsGroup.isVisible = state.isDetailsVisible
        binding.detailsEmpty.isVisible = state.isEmptyVisible
    }
}
