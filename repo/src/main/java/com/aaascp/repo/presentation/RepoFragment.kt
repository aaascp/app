package com.aaascp.repo.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation.findNavController
import androidx.paging.ExperimentalPagingApi
import com.aaascp.commons.Orientation
import com.aaascp.commons.safeNavigate
import com.aaascp.repo.R
import com.aaascp.repo.databinding.FragmentRepoBinding

@ExperimentalPagingApi
class RepoFragment : Fragment() {

    private var _binding: FragmentRepoBinding? = null
    private val binding get() = _binding!!
    private val repoListViewModel: RepoListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRepoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repoListViewModel.onViewCreated()
        repoListViewModel.selectedRepo.observe(this, ::handleSelectedItem)
    }

    private fun handleSelectedItem(repoSelectedState: RepoSelectedState) {
        if (repoSelectedState is RepoSelectedState.NoneSelected) {
            return
        }

        if (repoSelectedState.orientation == Orientation.LANDSCAPE) {
            return
        }

        findNavController(binding.root)
            .safeNavigate(R.id.action_RepoFragment_to_RepoDetailsFragment)
    }
}
