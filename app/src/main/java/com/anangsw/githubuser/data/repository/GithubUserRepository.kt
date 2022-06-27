package com.anangsw.githubuser.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anangsw.githubuser.data.cache.model.User
import kotlinx.coroutines.flow.Flow

interface GithubUserRepository {

    fun letUsersFlow(): Flow<PagingData<User>>

    suspend fun letUserFlow(username: String): Flow<Resource<User>>

}