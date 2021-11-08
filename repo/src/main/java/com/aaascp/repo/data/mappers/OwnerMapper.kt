package com.aaascp.repo.data.mappers

import com.aaascp.repo.data.entities.OwnerEntity
import com.aaascp.repo.data.responses.RepoResponse

class OwnerMapper {

    fun dtoToEntity(response: RepoResponse) = OwnerEntity(
        id = response.owner.id,
        name = response.owner.login,
        avatarUrl = response.owner.avatarUrl
    )
}
