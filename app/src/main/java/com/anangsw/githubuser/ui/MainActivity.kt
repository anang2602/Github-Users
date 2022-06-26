package com.anangsw.githubuser.ui

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.anangsw.githubuser.R
import com.anangsw.githubuser.base.BaseActivity
import com.anangsw.githubuser.databinding.ActivityMainBinding
import com.anangsw.githubuser.ui.adapter.GithubUsersAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(
    R.layout.activity_main
) {

    private val viewModel: MainViewModel by viewModels { factory }
    private lateinit var adapter: GithubUsersAdapter

    override fun onInitDataBinding() {
        super.onInitDataBinding()
        adapter = GithubUsersAdapter(viewModel)
        viewBinding.adapter = adapter
        fetchUser()
    }

    private fun fetchUser() {
        lifecycleScope.launch {
            viewModel.fetchUsers().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

}