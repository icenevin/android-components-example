package com.example.components.architecture.nvice.ui.user.details

import android.arch.lifecycle.ViewModel
import com.example.components.architecture.nvice.di.ViewModelKey
import com.example.components.architecture.nvice.di.scope.java.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class UserDetailsModule {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeUserDetailsFragment(): UserDetailsFragment

    @Binds
    @IntoMap
    @ViewModelKey(UserDetailsViewModel::class)
    internal abstract fun bindUserDetailsViewModel(viewModel: UserDetailsViewModel): ViewModel
}