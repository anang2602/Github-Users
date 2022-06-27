package com.anangsw.githubuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.anangsw.githubuser.data.cache.model.User
import com.anangsw.githubuser.databinding.ItemUsersBinding
import com.anangsw.githubuser.ui.MainViewModel
import com.anangsw.githubuser.utils.extentions.loadAvatar

class GithubUsersAdapter(private val viewModel: MainViewModel) :
    PagingDataAdapter<User, GithubUsersAdapter.ViewHolder>(COMPARATOR) {

    class ViewHolder(
        private val binding: ItemUsersBinding,
        private val viewModel: MainViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(user: User?) {
            binding.user = user
            binding.ivAvatar.loadAvatar(itemView.context, user?.avatar ?: "")
            binding.root.setOnClickListener {
                user?.let { it1 -> viewModel.onItemUserClick(it1.username) }
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.onBind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, viewModel)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }


}