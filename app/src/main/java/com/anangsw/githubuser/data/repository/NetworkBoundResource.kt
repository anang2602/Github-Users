package com.anangsw.githubuser.data.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.anangsw.githubuser.data.remote.dto.ApiEmptyResponse
import com.anangsw.githubuser.data.remote.dto.ApiErrorResponse
import com.anangsw.githubuser.data.remote.dto.ApiResponse
import com.anangsw.githubuser.data.remote.dto.ApiSuccessResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    fun asFlow() = flow {
        emit(Resource.loading(null))
        val dbValue = loadFromDb().firstOrNull()
        try {
            if (shouldFetch(dbValue)) {
                emit(Resource.loading(dbValue))
                when (val apiResponse = fetchFromNetwork()) {
                    is ApiSuccessResponse -> {
                        saveNetworkResult(processResponse(apiResponse))
                        emitAll(loadFromDb().map { Resource.success(it) })
                    }
                    is ApiErrorResponse -> {
                        onFetchFailed()
                        emitAll(loadFromDb().map {
                            Resource.error(
                                apiResponse.errorMessage,
                                it
                            )
                        })
                    }
                    is ApiEmptyResponse -> {
                        emitAll(loadFromDb().map { Resource.success(it) })
                    }
                }
            } else {
                emitAll(loadFromDb().map { Resource.success(it) })
            }
        } catch (e: Exception) {
            onFetchFailed()
            emitAll(loadFromDb().map {
                Resource.error(
                    e.localizedMessage!!,
                    it
                )
            })
        }
    }.flowOn(Dispatchers.IO)

    protected open fun onFetchFailed() {}

    @WorkerThread
    abstract suspend fun saveNetworkResult(item: RequestType)

    @WorkerThread
    protected fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @WorkerThread
    protected abstract suspend fun fetchFromNetwork(): ApiResponse<RequestType>

    @MainThread
    protected abstract fun loadFromDb(): Flow<ResultType>
}

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}