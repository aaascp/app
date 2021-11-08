package com.aaascp.repo.di

import android.content.Context
import androidx.room.withTransaction
import com.aaascp.repo.db.RepoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

typealias TransactionalContext = suspend (suspend () -> Unit) -> Unit

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RepoDatabase {
        return RepoDatabase.getInstance(context)
    }

    @Provides
    fun provideTransactionalContext(
        database: RepoDatabase
    ): TransactionalContext {
        return { body -> database.withTransaction { body() } }
    }
}
