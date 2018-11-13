package com.example.components.architecture.nvice.di.module

import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.ui.user.UserDataSourceFactory
import dagger.Module
import dagger.Provides

@Module
class DataSourceFactoryModule {

    @Provides
    fun provideUserDataSourceFactory(userDao: UserDao): UserDataSourceFactory {
        return UserDataSourceFactory(userDao)
    }
}