package com.example.components.architecture.nvice.ui.user


import android.arch.lifecycle.ViewModel
import com.example.components.architecture.nvice.di.ViewModelKey
import com.example.components.architecture.nvice.di.scope.java.FragmentScope
import com.example.components.architecture.nvice.ui.user.filter.UserFiltersFragment
import com.example.components.architecture.nvice.ui.user.filter.UserFiltersViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap


@Module
internal abstract class UserModule {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeUserFragment(): UserFragment

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeUserFiltersFragment(): UserFiltersFragment

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(viewModel: UserViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserFiltersViewModel::class)
    abstract fun bindUserFiltersViewModel(viewModel: UserFiltersViewModel): ViewModel
}