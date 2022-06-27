package com.anangsw.githubuser.di

import android.content.Context
import com.anangsw.githubuser.GithubUserApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        UIBinder::class,
        AppModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<GithubUserApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }

}