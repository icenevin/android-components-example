package com.example.components.architecture.nvice.di

import android.arch.persistence.room.Room
import android.content.Context
import android.net.wifi.WifiManager
import com.example.components.architecture.nvice.BaseApplication
import com.example.components.architecture.nvice.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: BaseApplication): Context =
            application.applicationContext

    @Singleton
    @Provides
    fun provideDatabase(application: BaseApplication) =
            Room.databaseBuilder(application, AppDatabase::class.java, "app.db")
                    .fallbackToDestructiveMigration()
                    .build()

    @Provides
    fun providesWifiManager(context: Context): WifiManager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
}