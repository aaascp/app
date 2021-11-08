package com.aaascp.repo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val REPO_STARTING_PAGE_INDEX = 1

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey
    val repoId: Long,
    val prevKey: Int?,
    val nextKey: Int?
) {
    constructor(repoId: Long, page: Int, isEndOfPagination: Boolean) : this(
        repoId = repoId,
        prevKey = if (page == REPO_STARTING_PAGE_INDEX) null else page - 1,
        nextKey = if (isEndOfPagination) null else page + 1
    )
}
