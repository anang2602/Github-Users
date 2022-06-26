package com.anangsw.githubuser.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserRemoteKeys(
    @PrimaryKey
    val username: String,
    val prevKey: Int?,
    val nextKey: Int?
)