package com.example.components.architecture.nvice.db

import androidx.room.Room
import com.example.components.architecture.nvice.BaseApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideUserDatabase(application: BaseApplication) =
            Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.NAME)
                    .fallbackToDestructiveMigration()
                    .build()
}