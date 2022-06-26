package com.anangsw.githubuser.data.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anangsw.githubuser.data.cache.model.User

@Dao
interface GithubUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(listUser: List<User>)

    @Query("SELECT * FROM user")
    fun getUsers(): PagingSource<Int, User>

    @Query("DELETE FROM user")
    fun clearUsers()

}