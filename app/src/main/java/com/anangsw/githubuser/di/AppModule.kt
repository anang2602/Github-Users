package com.anangsw.githubuser.di

import android.content.Context
import androidx.room.Room
import com.anangsw.githubuser.data.cache.GithubUserDatabase
import com.anangsw.githubuser.data.remote.GithubService
import com.anangsw.githubuser.data.remote.OkhttpClientBuilder
import com.anangsw.githubuser.data.remote.RetrofitBuilder
import com.anangsw.githubuser.data.repository.GithubUserRepository
import com.anangsw.githubuser.data.repository.datasource.GithubDatasource
import com.anangsw.githubuser.data.repository.remotemediator.GithubUserRemoteMediator
import com.anangsw.githubuser.utils.DispatcherHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @Singleton
    @Provides
    fun provideDispatcherHelper() = DispatcherHelper

    @Singleton
    @Provides
    fun provideGithubService(
    ) = RetrofitBuilder(OkhttpClientBuilder()).buildApi(GithubService::class.java)

    @Singleton
    @Provides
    fun provideDatabase(
        context: Context
    ) = Room.databaseBuilder(
        context, GithubUserDatabase::class.java,
        "github_user-db"
    ).build()

    @Singleton
    @Provides
    fun provideRemoteMediator(
        githubService: GithubService,
        githubUserDatabase: GithubUserDatabase
    ) = GithubUserRemoteMediator(githubService, githubUserDatabase)

    @Singleton
    @Provides
    fun provideGithubRepository(
        db: GithubUserDatabase,
        remoteMediator: GithubUserRemoteMediator
    ): GithubUserRepository = GithubDatasource(db, remoteMediator)

}