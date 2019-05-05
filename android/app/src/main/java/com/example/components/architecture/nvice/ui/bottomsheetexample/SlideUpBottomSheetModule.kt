package com.example.components.architecture.nvice.ui.bottomsheetexample

import androidx.lifecycle.ViewModel
import com.example.components.architecture.nvice.di.ViewModelKey
import com.example.components.architecture.nvice.di.scope.java.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class SlideUpBottomSheetModule{

    @FragmentScope
    @ContributesAndroidInjector()
    internal abstract fun contributeSlideUpBottomSheetFragment(): SlideUpBottomSheetFragment

    @Binds
    @IntoMap
    @ViewModelKey(SlideUpBottomSheetViewModel::class)
    abstract fun bindSlideUpBottomSheetViewModel(viewModel: SlideUpBottomSheetViewModel): ViewModel
}