package com.aaascp.repo.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.aaascp.repo.data.entities.REPO_STARTING_PAGE_INDEX
import com.aaascp.repo.data.entities.RemoteKey
import com.aaascp.repo.data.entities.dto.RepoOwnerEntityDto
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class RepoRemoteMediator(
    private val language: String,
    private val sort: String,
    private val repoPagingRepository: RepoPagingRepository,
) : RemoteMediator<Int, RepoOwnerEntityDto>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RepoOwnerEntityDto>
    ): MediatorResult {
        val pageResult = getPage(loadType, state)

        if (pageResult.page == null) {
            return MediatorResult.Success(pageResult.endOfPaginationReached)
        }

        return try {
            val endOfPaginationReached = if (loadType == LoadType.REFRESH) {
                repoPagingRepository.refreshAndSavePage(language, sort, pageResult.page, state.config.pageSize)
            } else {
                repoPagingRepository.savePage(language, sort, pageResult.page, state.config.pageSize)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getPage(
        loadType: LoadType,
        state: PagingState<Int, RepoOwnerEntityDto>
    ): PageResult {
        return when (loadType) {
            LoadType.REFRESH -> getPageResultForRefresh(state)
            LoadType.PREPEND -> getPageResultForPrepend(state)
            LoadType.APPEND -> getPageResultForAppend(state)
        }
    }

    private suspend fun getPageResultForAppend(state: PagingState<Int, RepoOwnerEntityDto>): PageResult {
        val repo = getLastRepo(state)
        val remoteKeys = repoPagingRepository.getRemoteKeyForRepo(repo?.id)
        return PageResult(remoteKeys, remoteKeys?.nextKey)
    }

    private suspend fun getPageResultForPrepend(state: PagingState<Int, RepoOwnerEntityDto>): PageResult {
        val repo = getFirstRepo(state)
        val remoteKeys = repoPagingRepository.getRemoteKeyForRepo(repo?.id)
        return PageResult(remoteKeys, remoteKeys?.prevKey)
    }

    private suspend fun getPageResultForRefresh(state: PagingState<Int, RepoOwnerEntityDto>): PageResult {
        val repo = getClosestRepoToCurrentPosition(state)
        val remoteKeys = repoPagingRepository.getRemoteKeyForRepo(repo?.id)
        val key = remoteKeys?.nextKey?.minus(1) ?: REPO_STARTING_PAGE_INDEX
        return PageResult(remoteKeys, key)
    }
}

private fun getLastRepo(state: PagingState<Int, RepoOwnerEntityDto>): RepoOwnerEntityDto? {
    return state.pages.lastOrNull { it.data.isNotEmpty() }
        ?.data
        ?.lastOrNull()
}

private fun getFirstRepo(state: PagingState<Int, RepoOwnerEntityDto>): RepoOwnerEntityDto? {
    return state.pages.firstOrNull { it.data.isNotEmpty() }
        ?.data
        ?.firstOrNull()
}

private fun getClosestRepoToCurrentPosition(
    state: PagingState<Int, RepoOwnerEntityDto>
): RepoOwnerEntityDto? {
    return state.anchorPosition?.let { position ->
        state.closestItemToPosition(position)
    }
}

class PageResult(
    remoteKeys: RemoteKey?,
    val page: Int?
) {
    val endOfPaginationReached = remoteKeys != null
}
