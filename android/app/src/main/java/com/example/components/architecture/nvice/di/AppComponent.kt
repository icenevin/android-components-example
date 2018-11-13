package com.example.components.architecture.nvice.di

import com.example.components.architecture.nvice.BaseApplication
import com.example.components.architecture.nvice.di.module.DaoModule
import com.example.components.architecture.nvice.di.module.DataSourceFactoryModule
import com.example.components.architecture.nvice.di.module.PreferenceModule
import com.example.components.architecture.nvice.di.module.UserGeneratorModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            DaoModule::class,
            UserGeneratorModule::class,
            ActivityBindingModule::class,
            DataSourceFactoryModule::class,
            ViewModelModule::class,
            PreferenceModule::class
        ])
interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<BaseApplication>()
}