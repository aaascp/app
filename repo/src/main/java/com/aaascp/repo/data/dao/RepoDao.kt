package com.aaascp.repo.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aaascp.repo.data.entities.RepoEntity
import com.aaascp.repo.data.entities.dto.RepoOwnerEntityDto

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repos: List<RepoEntity>)

    @Query(
        """
            SELECT
                repos.id AS id,
                repos.name AS name,
                repos.full_name AS fullName,
                repos.description AS description,
                repos.url AS url,
                repos.stars AS stars,
                repos.forks_count AS forks,
                repos.language AS language,
                owners.name AS ownerName,
                owners.avatar_url AS ownerAvatarUrl
            FROM repos
            JOIN owners ON repos.owner_id = owners.id
            ORDER BY stars DESC
        """
    )
    fun findAll(): PagingSource<Int, RepoOwnerEntityDto>

    @Query("DELETE FROM repos")
    suspend fun clearRepos()
}
