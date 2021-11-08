package com.aaascp.repo.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aaascp.commons.load
import com.aaascp.repo.databinding.RowRepoListBinding
import com.aaascp.repo.domain.Repo

class RepoListAdapter(
    private val onRepoSelectedListener: OnRepoSelectedListener
): PagingDataAdapter<Repo, RepoListViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowRepoListBinding.inflate(layoutInflater, parent, false)
        return RepoListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoListViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem, onRepoSelectedListener)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean =
                oldItem == newItem
        }
    }
}

class RepoListViewHolder(
    private val binding: RowRepoListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Repo, onRepoSelectedListener: OnRepoSelectedListener) {
        with(binding) {
            cardAvatar?.load(item.owner.avatarUrl)
            cardStars.text = item.stars.toString()
            cardTitle.text = item.name
            cardAuthor.text = item.owner.name
            rootLayout.setOnClickListener { onRepoSelectedListener.onItemSelected(item) }
        }
    }
}
