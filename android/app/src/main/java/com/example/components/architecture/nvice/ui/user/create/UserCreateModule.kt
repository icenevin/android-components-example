package com.example.components.architecture.nvice.ui.user.create

import android.arch.lifecycle.ViewModel
import com.example.components.architecture.nvice.di.ViewModelKey
import com.example.components.architecture.nvice.di.scope.java.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class UserCreateModule {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeUserCreateFragment(): UserCreateFragment

    @Binds
    @IntoMap
    @ViewModelKey(UserCreateViewModel::class)
    internal abstract fun bindUserCreateViewModel(viewModel: UserCreateViewModel): ViewModel
}