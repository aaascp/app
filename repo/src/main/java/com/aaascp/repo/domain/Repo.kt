package com.aaascp.repo.domain

data class Repo(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val url: String,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val owner: Owner
)

data class Owner(
    val name: String,
    val avatarUrl: String
)
