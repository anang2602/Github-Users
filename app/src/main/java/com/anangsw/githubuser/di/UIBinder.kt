package com.anangsw.githubuser.di

import com.anangsw.githubuser.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIBinder {

    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity

}