package com.aaascp.repo.data.responses

class RepoListResponse(
    val items: List<RepoResponse>
)

data class RepoResponse(
    val id: Long,
    val name: String,
    val watchers: Int,
    val forks: Int,
    val owner: OwnerResponse,
    val fullName: String,
    val description: String?,
    val url: String,
    val stargazersCount: Int,
    val language: String?
)

data class OwnerResponse(
    val login: String,
    val avatarUrl: String,
    val id: Long
)
