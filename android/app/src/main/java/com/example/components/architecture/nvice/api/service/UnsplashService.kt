package com.example.components.architecture.nvice.api.service

import com.example.components.architecture.nvice.api.response.UiNamesResponse
import com.example.components.architecture.nvice.api.response.UnsplashPhotosResponse
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashService {

    @GET("photos/random/")
    fun getPhotosRandom(
            @Query("client_id") clientId: String,
            @Query("count") count: String
    ) : Flowable<List<UnsplashPhotosResponse>>
}