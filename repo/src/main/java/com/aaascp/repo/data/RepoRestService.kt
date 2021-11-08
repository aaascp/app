package com.aaascp.repo.data;

import com.aaascp.repo.data.responses.RepoListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoRestService {
    @GET("repositories")
    suspend fun listRepos(
        @Query("q") language: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): RepoListResponse
}



