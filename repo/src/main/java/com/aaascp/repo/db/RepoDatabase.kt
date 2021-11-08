package com.aaascp.repo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aaascp.repo.data.dao.OwnerDao
import com.aaascp.repo.data.dao.RemoteKeysDao
import com.aaascp.repo.data.dao.RepoDao
import com.aaascp.repo.data.entities.OwnerEntity
import com.aaascp.repo.data.entities.RemoteKey
import com.aaascp.repo.data.entities.RepoEntity

@Database(
    entities = [
        RepoEntity::class,
        OwnerEntity::class,
        RemoteKey::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RepoDatabase : RoomDatabase() {

    abstract fun reposDao(): RepoDao
    abstract fun ownersDao(): OwnerDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        private const val DATABASE_NAME = "repo.db"

        @Volatile
        private var INSTANCE: RepoDatabase? = null

        fun getInstance(context: Context): RepoDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RepoDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}
