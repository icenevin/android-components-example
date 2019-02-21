package com.example.components.architecture.nvice.di.module

import com.example.components.architecture.nvice.api.ApiConfigs
import com.example.components.architecture.nvice.api.service.UiFacesService
import com.example.components.architecture.nvice.api.service.UiNamesService
import com.example.components.architecture.nvice.api.service.UnsplashService
import com.example.components.architecture.nvice.util.UserGenerator
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Module
class UserGeneratorModule {

    @Singleton
    @Provides
    fun provideUiNamesService(okHttpClientBuilder: OkHttpClient.Builder): UiNamesService {

        val okHttpClient = okHttpClientBuilder
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
    fun provideUiFacesService(okHttpClientBuilder: OkHttpClient.Builder): UiFacesService {

        val okHttpClient = okHttpClientBuilder
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Accept", "text/html")
                            .addHeader("X-API-KEY", ApiConfigs.UNIFACE_X_API_KEY)
                            .method(original.method(), original.body())
                            .build()
                    chain.proceed(request)
                }
                .connectTimeout(5000, TimeUnit.MILLISECONDS)

                .retryOnConnectionFailure(true)
                .build()

        return Retrofit.Builder()
                .baseUrl("https://uifaces.co/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okHttpClient)
                .build()
                .create(UiFacesService::class.java)
    }

    @Singleton
    @Provides
    fun provideUnsplashService(okHttpClientBuilder: OkHttpClient.Builder): UnsplashService {

        val okHttpClient = okHttpClientBuilder
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                            .header("Authorization", "Client-ID ${ApiConfigs.UNSPLASH_ACCESS_KEY}")
                            .method(original.method(), original.body())
                            .build()
                    chain.proceed(request)
                }
                .retryOnConnectionFailure(true)
                .build()

        return Retrofit.Builder()
                .baseUrl("https://api.unsplash.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(UnsplashService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserGenerator(
            uiNamesService: UiNamesService,
            uiFacesService: UiFacesService,
            unsplashService: UnsplashService
    ): UserGenerator = UserGenerator(uiNamesService, uiFacesService, unsplashService)
}