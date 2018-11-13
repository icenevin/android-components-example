package com.example.components.architecture.nvice.di.module

import com.example.components.architecture.nvice.BaseApplication
import com.example.components.architecture.nvice.data.preference.AppSettingsPreference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PreferenceModule {

    @Singleton
    @Provides
    fun provideAppSettingsPreference(application: BaseApplication) = AppSettingsPreference(application.baseContext)
}