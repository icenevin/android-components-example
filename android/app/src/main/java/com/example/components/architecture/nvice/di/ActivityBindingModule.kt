package com.example.components.architecture.nvice.di

import com.example.components.architecture.nvice.ui.MainActivity
import com.example.components.architecture.nvice.di.scope.java.ActivityScope
import com.example.components.architecture.nvice.ui.user.UserModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(
            modules = [
                UserModule::class
            ])
    internal abstract fun mainActivity(): MainActivity
}