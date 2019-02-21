package com.example.components.architecture.nvice.di

import com.example.components.architecture.nvice.ui.MainActivity
import com.example.components.architecture.nvice.di.scope.java.ActivityScope
import com.example.components.architecture.nvice.ui.camera.CameraActivity
import com.example.components.architecture.nvice.ui.camera.CameraModule
import com.example.components.architecture.nvice.ui.user.UserModule
import com.example.components.architecture.nvice.ui.user.create.UserCreateActivity
import com.example.components.architecture.nvice.ui.user.create.UserCreateModule
import com.example.components.architecture.nvice.ui.user.details.UserDetailsActivity
import com.example.components.architecture.nvice.ui.user.details.UserDetailsModule
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

    @ActivityScope
    @ContributesAndroidInjector(
            modules = [
                CameraModule::class
            ]
    )
    internal abstract fun cameraActivity(): CameraActivity

    @ActivityScope
    @ContributesAndroidInjector(
            modules = [
                UserDetailsModule::class
            ]
    )
    internal abstract fun userDetailsActivity(): UserDetailsActivity

    @ActivityScope
    @ContributesAndroidInjector(
            modules = [
                UserCreateModule::class
            ]
    )
    internal abstract fun userCreateActivity(): UserCreateActivity
}