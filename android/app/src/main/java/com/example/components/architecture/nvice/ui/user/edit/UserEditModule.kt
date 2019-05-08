package com.example.components.architecture.nvice.ui.user.edit

import androidx.lifecycle.ViewModel
import com.example.components.architecture.nvice.di.ViewModelKey
import com.example.components.architecture.nvice.di.scope.java.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class UserEditModule {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeUserEditFragment(): UserEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(UserEditViewModel::class)
    internal abstract fun bindUserEditViewModel(viewModel: UserEditViewModel): ViewModel
}