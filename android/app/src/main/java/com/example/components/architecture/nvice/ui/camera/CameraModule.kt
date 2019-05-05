package com.example.components.architecture.nvice.ui.camera

import androidx.lifecycle.ViewModel
import com.example.components.architecture.nvice.di.ViewModelKey
import com.example.components.architecture.nvice.di.scope.java.FragmentScope
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class CameraModule {

    @FragmentScope
    @ContributesAndroidInjector
    internal abstract fun contributeCameraFragment(): CameraFragment

    @Binds
    @IntoMap
    @ViewModelKey(CameraViewModel::class)
    internal abstract fun bindCameraViewModel(viewModel: CameraViewModel): ViewModel
}