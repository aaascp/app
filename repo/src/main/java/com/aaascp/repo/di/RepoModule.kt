package com.aaascp.repo.di

import com.aaascp.repo.data.RepoRestService
import com.aaascp.repo.data.dao.OwnerDao
import com.aaascp.repo.data.dao.RemoteKeysDao
import com.aaascp.repo.data.dao.RepoDao
import com.aaascp.repo.db.RepoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Qualifier

private const val GITHUB_BASE_URL = "https://api.github.com/search/"

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GithubRestServiceRetrofit

@InstallIn(SingletonComponent::class)
@Module
object RepoModule {

    @Provides
    fun provideRepoRestService(
        @GithubRestServiceRetrofit retrofit: Retrofit
    ): RepoRestService {
        return retrofit.create(RepoRestService::class.java)
    }

    @GithubRestServiceRetrofit
    @Provides
    fun provideGithubServiceRetrofit(
        retrofitBuilder: Retrofit.Builder
    ): Retrofit {
        return retrofitBuilder
            .baseUrl(GITHUB_BASE_URL)
            .build()
    }

    @Provides
    fun provideRepoDao(
        database: RepoDatabase
    ): RepoDao {
        return database.reposDao()
    }

    @Provides
    fun provideOwnerDao(
        database: RepoDatabase
    ): OwnerDao {
        return database.ownersDao()
    }

    @Provides
    fun provideRemoteKeysDao(
        database: RepoDatabase
    ): RemoteKeysDao {
        return database.remoteKeysDao()
    }
}
