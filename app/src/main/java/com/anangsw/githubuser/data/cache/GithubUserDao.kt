package com.anangsw.githubuser.data.cache

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anangsw.githubuser.data.cache.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(listUser: List<User>)

    @Query("UPDATE user SET email = :email, created =:created  WHERE id=:id ")
    fun updateUser(id: Int, email: String, created: String)

    @Query("SELECT * FROM user")
    fun getUsers(): PagingSource<Int, User>

    @Query("SELECT * FROM user WHERE username = :username")
    fun getUser(username: String): Flow<User>

    @Query("DELETE FROM user")
    fun clearUsers()

}