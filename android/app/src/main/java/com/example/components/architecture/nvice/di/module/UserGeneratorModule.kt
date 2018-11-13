package com.example.components.architecture.nvice.di.module

import com.example.components.architecture.nvice.api.service.UiNamesService
import com.example.components.architecture.nvice.util.UserGenerator
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class UserGeneratorModule {

    @Singleton
    @Provides
    fun provideUiNamesService(): UiNamesService {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .build()

        return Retrofit.Builder()
                .baseUrl("https://uinames.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(UiNamesService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserGenerator(uiNamesService: UiNamesService): UserGenerator = UserGenerator(uiNamesService)
}