package com.anangsw.githubuser.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anangsw.githubuser.data.cache.model.User
import com.anangsw.githubuser.data.repository.GithubUserRepository
import com.anangsw.githubuser.utils.DispatcherHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: GithubUserRepository,
    private val dispatcherHelper: DispatcherHelper
): ViewModel() {


    fun onItemUserClick() {

    }

    fun fetchUsers(): Flow<PagingData<User>> {
        return repository.letUsersFlow().cachedIn(viewModelScope)
    }

}