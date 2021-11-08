package com.aaascp.repo.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "repos",
    foreignKeys = [
        ForeignKey(
            entity = OwnerEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("owner_id")
        )
    ]
)
data class RepoEntity(
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    val description: String?,
    val url: String,
    val stars: Int,
    @ColumnInfo(name= "forks_count") val forks: Int,
    val language: String?,
    @ColumnInfo(name="owner_id", index = true) val ownerId: Long
)
