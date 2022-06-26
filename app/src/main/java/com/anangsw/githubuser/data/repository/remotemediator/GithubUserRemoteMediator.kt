@file:OptIn(ExperimentalPagingApi::class)

package com.anangsw.githubuser.data.repository.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.anangsw.githubuser.data.cache.GithubUserDatabase
import com.anangsw.githubuser.data.cache.model.User
import com.anangsw.githubuser.data.cache.model.UserRemoteKeys
import com.anangsw.githubuser.data.remote.GithubService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GithubUserRemoteMediator @Inject constructor(
    private val githubService: GithubService,
    private val db: GithubUserDatabase
) : RemoteMediator<Int, User>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {

        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData?.toString()?.toInt() ?: 0
            }
        }
        try {
            val users = githubService.getUsers(page, state.config.pageSize)
            val isEndOfList = users.isEmpty()
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.GithubUserDao().clearUsers()
                    db.GithubUserRemoteKeys().clearRemoteKeys()
                }
                val keys = mutableListOf<UserRemoteKeys>()
                for ((idx, user) in users.withIndex()) {
                    val prevKey = if (page == 0 || idx == 0) null else users[idx - 1].id
                    val nextKey = if (isEndOfList) null else user.id
                    keys.add(
                        UserRemoteKeys(user.login, prevKey, nextKey)
                    )
                }
                db.GithubUserRemoteKeys().insertAll(keys)
                val usersRows = users.map {
                    User(
                        id = it.id,
                        username = it.login,
                        avatar = it.avatar_url,
                        repo = it.repos_url
                    )
                }
                db.GithubUserDao().insertUsers(usersRows)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (io: IOException) {
            return MediatorResult.Error(io)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }

    }

    private suspend fun getKeyPageData(
        loadType: LoadType,
        state: PagingState<Int, User>
    ): Any? {
        return when (loadType) {
            LoadType.REFRESH -> {
                0
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                remoteKeys?.nextKey
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                remoteKeys?.prevKey ?: MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, User>): UserRemoteKeys? {
        return state.pages
            .firstOrNull { it.data.isEmpty() }
            ?.data?.firstOrNull()
            ?.let { user ->
                db.GithubUserRemoteKeys().remoteKeysId(
                    user.username
                )
            }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, User>): UserRemoteKeys? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { user ->
                db.GithubUserRemoteKeys().remoteKeysId(user.username)
            }
    }

}