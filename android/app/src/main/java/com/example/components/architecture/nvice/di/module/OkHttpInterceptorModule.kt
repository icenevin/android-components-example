package com.example.components.architecture.nvice.di.module

import com.example.components.architecture.nvice.api.logger.HttpOptimizedLogger
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class OkHttpInterceptorModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor(HttpOptimizedLogger())
            .setLevel(HttpLoggingInterceptor.Level.BODY)
}
