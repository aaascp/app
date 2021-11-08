package com.aaascp.repo.usecases

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.aaascp.commons.di.UseCaseDispatcher
import com.aaascp.repo.data.RepoRepository
import com.aaascp.repo.domain.Repo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val LANGUAGE_KOTLIN_TAG = "kotlin"
private const val SORT_TAG = "stars"

@ExperimentalPagingApi
class GetAllKotlinReposOrderedByStarsUseCase @Inject constructor(
    private val repoRepository: RepoRepository,
    @UseCaseDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun execute(): Flow<PagingData<Repo>> = withContext(dispatcher) {
        repoRepository.findByLanguage(LANGUAGE_KOTLIN_TAG, SORT_TAG)
    }
}
