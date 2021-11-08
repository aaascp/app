package com.aaascp.repo.data.entities.dto

data class RepoOwnerEntityDto(
    val id: Long,
    val name: String,
    val fullName: String,
    val description: String?,
    val url: String,
    val stars: Int,
    val forks: Int,
    val language: String?,
    val ownerName: String,
    val ownerAvatarUrl: String
)
