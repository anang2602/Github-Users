package com.anangsw.githubuser.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anangsw.githubuser.data.cache.model.User
import com.anangsw.githubuser.data.repository.GithubUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: GithubUserRepository
) : ViewModel() {

    private val username = MutableLiveData<String>()

    val letUserFlow = username.switchMap { user ->
        liveData {
            val res = repository.letUserFlow(user).distinctUntilChanged().asLiveData(Dispatchers.Main)
            emitSource(res)
        }
    }

    fun onItemUserClick(name: String) {
        username.value = name
    }

    fun fetchUsers(): Flow<PagingData<User>> {
        return repository.letUsersFlow().cachedIn(viewModelScope)
    }

}