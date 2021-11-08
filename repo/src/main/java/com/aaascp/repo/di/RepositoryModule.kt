package com.aaascp.repo.di

import androidx.paging.ExperimentalPagingApi
import com.aaascp.repo.data.DefaultRepoRepository
import com.aaascp.repo.data.RepoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@ExperimentalPagingApi
@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepoRepository(
        repository: DefaultRepoRepository
    ): RepoRepository
}
