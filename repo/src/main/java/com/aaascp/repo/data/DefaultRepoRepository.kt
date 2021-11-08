package com.aaascp.repo.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.aaascp.commons.di.DispatcherIO
import com.aaascp.repo.data.dao.OwnerDao
import com.aaascp.repo.data.dao.RemoteKeysDao
import com.aaascp.repo.data.dao.RepoDao
import com.aaascp.repo.data.entities.RemoteKey
import com.aaascp.repo.data.mappers.OwnerMapper
import com.aaascp.repo.data.mappers.RepoMapper
import com.aaascp.repo.data.responses.RepoResponse
import com.aaascp.repo.db.RepoDatabase
import com.aaascp.repo.domain.Repo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 30
private const val LANGUAGE_SEARCH_TAG = "language"

@ExperimentalPagingApi
class DefaultRepoRepository @Inject constructor(
    private val database: RepoDatabase,
    private val repoRestService: RepoRestService,
    private val repoDao: RepoDao,
    private val ownerDao: OwnerDao,
    private val remoteKeysDao: RemoteKeysDao,
    @DispatcherIO private val dispatcherIO: CoroutineDispatcher
) : RepoRepository, RepoPagingRepository {

    override fun findByLanguage(language: String, sortBy: String): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = RepoRemoteMediator(
                "$LANGUAGE_SEARCH_TAG:$language",
                sortBy,
                this
            ),
            pagingSourceFactory = { repoDao.findAll() }
        ).flow
            .map { pagingData ->
                pagingData.map { RepoMapper().entityToDomain(it) }
            }
    }

    private suspend fun clearRepos() {
        withContext(dispatcherIO) {
            repoDao.clearRepos()
        }
    }

    private suspend fun insertAll(repos: List<RepoResponse>) {
        withContext(dispatcherIO) {
            ownerDao.insertAll(repos.map { OwnerMapper().dtoToEntity(it) })
            repoDao.insertAll(repos.map { RepoMapper().responseToEntity(it) })
        }
    }

    override suspend fun refreshAndSavePage(
        language: String,
        sort: String,
        page: Int,
        pageSize: Int
    ): Boolean {
        var endOfPaginationReached = false
        database.withTransaction {
            clearRemoteKeys()
            clearRepos()
            endOfPaginationReached = savePage(language, sort, page, pageSize)
        }
        return endOfPaginationReached
    }

    override suspend fun savePage(
        language: String,
        sort: String,
        page: Int,
        pageSize: Int
    ): Boolean {
        val repos = getReposFromRestService(language, sort, page, pageSize)
        val endOfPaginationReached = repos.isEmpty()

        database.withTransaction {
            val keys = repos.map {
                RemoteKey(repoId = it.id, page = page, isEndOfPagination = endOfPaginationReached)
            }
            insertAllRemoteKeys(keys)
            insertAll(repos)
        }

        return endOfPaginationReached
    }

    override suspend fun getRemoteKeyForRepo(repoId: Long?): RemoteKey? {
        if (repoId == null) {
            return null
        }


        return remoteKeysDao.remoteKeysRepoId(repoId)
    }

    private suspend fun clearRemoteKeys() {
        withContext(dispatcherIO) {
            remoteKeysDao.clearRemoteKeys()
        }
    }

    private suspend fun insertAllRemoteKeys(keys: List<RemoteKey>) {
        withContext(dispatcherIO) {
            remoteKeysDao.insertAll(keys)
        }
    }

    private suspend fun getReposFromRestService(
        language: String,
        sort: String,
        page: Int,
        pageSize: Int
    ): List<RepoResponse> = withContext(dispatcherIO) {
        return@withContext repoRestService.listRepos(language, sort, page, pageSize).items
    }

}

interface RepoRepository {
    fun findByLanguage(language: String, sortBy: String): Flow<PagingData<Repo>>
}

interface RepoPagingRepository {
    suspend fun refreshAndSavePage(
        language: String,
        sort: String,
        page: Int,
        pageSize: Int
    ): Boolean

    suspend fun savePage(
        language: String,
        sort: String,
        page: Int,
        pageSize: Int
    ): Boolean

    suspend fun getRemoteKeyForRepo(repoId: Long?): RemoteKey?
}
