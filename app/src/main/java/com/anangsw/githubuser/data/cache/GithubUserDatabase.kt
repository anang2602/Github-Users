package com.anangsw.githubuser.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anangsw.githubuser.data.cache.model.User
import com.anangsw.githubuser.data.cache.model.UserRemoteKeys

@Database(
    entities = [
        User::class,
        UserRemoteKeys::class
    ],
    exportSchema = false,
    version = 1
)
abstract class GithubUserDatabase: RoomDatabase() {

    abstract fun GithubUserDao(): GithubUserDao

    abstract fun GithubUserRemoteKeys(): GithubUserRemoteKeys

}