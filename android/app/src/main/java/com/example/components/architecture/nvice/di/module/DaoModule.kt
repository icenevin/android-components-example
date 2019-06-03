package com.example.components.architecture.nvice.di.module

import com.example.components.architecture.nvice.db.UserDatabase
import dagger.Module
import dagger.Provides

@Module
class DaoModule {
    @Provides
    fun provideUserDao(database: UserDatabase) = database.userDao()
}