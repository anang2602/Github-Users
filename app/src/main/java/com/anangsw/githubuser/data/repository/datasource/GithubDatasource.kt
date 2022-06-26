package com.anangsw.githubuser.data.repository.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anangsw.githubuser.data.cache.GithubUserDatabase
import com.anangsw.githubuser.data.cache.model.User
import com.anangsw.githubuser.data.remote.GithubService
import com.anangsw.githubuser.data.remote.dto.ApiResponse
import com.anangsw.githubuser.data.remote.dto.UserResponse
import com.anangsw.githubuser.data.repository.GithubUserRepository
import com.anangsw.githubuser.data.repository.NetworkBoundResource
import com.anangsw.githubuser.data.repository.Resource
import com.anangsw.githubuser.data.repository.remotemediator.GithubUserRemoteMediator
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GithubDatasource @Inject constructor(
    private val db: GithubUserDatabase,
    private val remoteMediator: GithubUserRemoteMediator,
    private val githubService: GithubService,
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

    override suspend fun letUserFlow(username: String): Flow<Resource<User>> {
        return object : NetworkBoundResource<User, UserResponse>() {
            override suspend fun saveNetworkResult(item: UserResponse) {
                db.GithubUserDao().updateUser(
                    item.id, item.email ?: "", item.created_at
                )
            }

            override fun shouldFetch(data: User?) = data?.email.isNullOrEmpty()

            override suspend fun fetchFromNetwork(): ApiResponse<UserResponse> {
                return try {
                    val response = githubService.getUser(username)
                    ApiResponse.create(response)
                } catch (e: IOException) {
                    ApiResponse.create(e)
                } catch (e: HttpException) {
                    ApiResponse.create(e)
                }
            }

            override fun loadFromDb(): Flow<User> {
                return db.GithubUserDao().getUser(username)
            }
        }.asFlow()
    }
}