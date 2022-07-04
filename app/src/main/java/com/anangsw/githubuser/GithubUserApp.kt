package com.anangsw.githubuser


import com.anangsw.githubuser.di.DaggerApplicationComponent
import com.google.android.material.color.DynamicColors
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class GithubUserApp: DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
//        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }
}