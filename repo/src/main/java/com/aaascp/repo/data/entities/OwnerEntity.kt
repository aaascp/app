package com.aaascp.repo.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "owners")
data class OwnerEntity(
    @PrimaryKey val id: Long,
    val name: String,
    @ColumnInfo(name ="avatar_url") val avatarUrl: String
)
