package com.anangsw.githubuser.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val id: Int,
    val username: String,
    val avatar: String?,
    val repo: String,
    val email: String? = null,
    val created: String? = null
)
