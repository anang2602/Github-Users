package com.anangsw.githubuser.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anangsw.githubuser.data.cache.model.UserRemoteKeys

@Dao
interface GithubUserRemoteKeys {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<UserRemoteKeys>)

    @Query("SELECT * FROM userremotekeys WHERE username = :username")
    suspend fun remoteKeysId(username: String): UserRemoteKeys?

    @Query("DELETE FROM userremotekeys")
    suspend fun clearRemoteKeys()

}