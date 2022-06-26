package com.anangsw.githubuser.data.repository.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anangsw.githubuser.data.cache.GithubUserDatabase
import com.anangsw.githubuser.data.cache.model.User
import com.anangsw.githubuser.data.repository.GithubUserRepository
import com.anangsw.githubuser.data.repository.remotemediator.GithubUserRemoteMediator
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubDatasource @Inject constructor(
    private val db: GithubUserDatabase,
    private val remoteMediator: GithubUserRemoteMediator
) : GithubUserRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun letUsersFlow(): Flow<PagingData<User>> {
        val pagingSourceFactory = { db.GithubUserDao().getUsers() }
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = true),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = remoteMediator
        ).flow
    }
}