package com.anangsw.githubuser.data.remote

import com.anangsw.githubuser.data.remote.dto.UserResponse
import com.anangsw.githubuser.data.remote.dto.UsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("/users")
    suspend fun getUsers(
        @Query("since") since: Int,
        @Query("per_page") pageSize: Int
    ): UsersResponse

    @GET("/users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): Response<UserResponse>

}