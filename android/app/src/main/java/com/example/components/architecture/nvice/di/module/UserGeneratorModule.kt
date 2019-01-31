package com.example.components.architecture.nvice.di.module

import com.example.components.architecture.nvice.api.service.UiFacesService
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

    var httpLoggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()

    init {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideUiNamesService(): UiNamesService {

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
    fun provideUiFacesService(): UiFacesService {

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Accept", "text/html")
                            .addHeader("X-API-KEY", "97dbebcc36ee1f2376e3886ce8982d")
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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(UiFacesService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserGenerator(uiNamesService: UiNamesService, uiFacesService: UiFacesService): UserGenerator = UserGenerator(uiNamesService, uiFacesService)
}