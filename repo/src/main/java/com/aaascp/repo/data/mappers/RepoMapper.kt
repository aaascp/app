package com.aaascp.repo.data.mappers

import com.aaascp.repo.data.entities.RepoEntity
import com.aaascp.repo.data.entities.dto.RepoOwnerEntityDto
import com.aaascp.repo.data.responses.RepoResponse
import com.aaascp.repo.domain.Owner
import com.aaascp.repo.domain.Repo

class RepoMapper {

    fun responseToEntity(response: RepoResponse) = RepoEntity(
        id = response.id,
        name = response.name,
        fullName = response.fullName,
        description = response.description,
        url = response.url,
        stars = response.stargazersCount,
        language = response.language,
        forks = response.forks,
        ownerId = response.owner.id
    )

    fun entityToDomain(entity: RepoOwnerEntityDto) = Repo(
        id = entity.id,
        name = entity.name,
        fullName = entity.fullName,
        description = entity.description,
        url = entity.url,
        stars = entity.stars,
        language = entity.language,
        forks = entity.forks,
        owner = Owner(
            name = entity.ownerName,
            avatarUrl = entity.ownerAvatarUrl
        )
    )
}
