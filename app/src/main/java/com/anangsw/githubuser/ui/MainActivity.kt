package com.anangsw.githubuser.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.anangsw.githubuser.R
import com.anangsw.githubuser.base.BaseActivity
import com.anangsw.githubuser.data.cache.model.User
import com.anangsw.githubuser.data.repository.Resource
import com.anangsw.githubuser.data.repository.Status
import com.anangsw.githubuser.databinding.ActivityMainBinding
import com.anangsw.githubuser.ui.adapter.GithubUsersAdapter
import com.anangsw.githubuser.utils.extentions.hide
import com.anangsw.githubuser.utils.extentions.observe
import com.anangsw.githubuser.utils.extentions.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
        observeAdapterState()
        observe(viewModel.letUserFlow, ::onUserClicked)

        viewBinding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun onUserClicked(resource: Resource<User>) {
        Log.d("ASD", "${resource.status} ${resource.message}")
        when (resource.status) {
            Status.SUCCESS -> {
                val msg =
                    "Username : ${resource.data?.username} , email : ${resource.data?.email}, created at : ${resource.data?.created}"
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
            Status.ERROR -> {
                showErrorSnackBar(viewBinding.root, resource.message.toString())
            }
            else -> {}
        }
    }

    private fun fetchUser() {
        lifecycleScope.launch {
            viewModel.fetchUsers().distinctUntilChanged().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun observeAdapterState() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadstate ->
                viewBinding.swipeRefresh.isRefreshing = loadstate.refresh is LoadState.Loading
                if (loadstate.refresh is LoadState.Loading && adapter.itemCount == 0) {
                    viewBinding.shimmer.startShimmer()
                    viewBinding.shimmer.show()
                } else {
                    viewBinding.shimmer.stopShimmer()
                    viewBinding.shimmer.hide()
                }
                when (val currstate = loadstate.refresh) {
                    is LoadState.Error -> {
                        if (currstate.error is HttpException) {
                            when ((currstate.error as HttpException).code()) {
                                401 -> {
                                    showErrorSnackBar(viewBinding.root, "Unauthorized")
                                }
                                403 -> {
                                    showErrorSnackBar(viewBinding.root, "Forbidden")
                                }
                            }
                        } else {
                            showErrorSnackBar(viewBinding.root, currstate.error.message.toString())
                        }
                    }
                    is LoadState.Loading -> {}
                    else -> {}
                }
            }
        }
    }

}