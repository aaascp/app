package com.aaascp.repo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.aaascp.repo.data.entities.OwnerEntity

@Dao
interface OwnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(owners: List<OwnerEntity>)
}
