package com.example.components.architecture.nvice.di

import com.example.components.architecture.nvice.BaseApplication
import com.example.components.architecture.nvice.di.module.*
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
            OkHttpInterceptorModule::class,
            OkHttpClientModule::class,
            UserGeneratorModule::class,
            ActivityBindingModule::class,
//            DataSourceFactoryModule::class,
//            PreferenceModule::class,
            ViewModelModule::class
        ])

interface AppComponent : AndroidInjector<BaseApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<BaseApplication>()
}