package com.aaascp.repo.di

import androidx.paging.PagingData
import com.aaascp.repo.data.RepoRepository
import com.aaascp.repo.domain.Repo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ExperimentalCoroutinesApi
class TestRepoRepository @Inject constructor(
    private val repos: List<Repo>
) : RepoRepository {

    override fun findByLanguage(language: String, sortBy: String): Flow<PagingData<Repo>> {
        return flowOf(PagingData.from(repos))
    }
}
