package com.example.components.architecture.nvice.api.service

import com.example.components.architecture.nvice.api.response.UiNamesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UiNamesService {

    @GET("api/")
    fun getUser() : Call<UiNamesResponse>

    @GET("api/")
    fun getUserInCountry(
            @Query("ext") extended: String,
            @Query("region") country: String
    ) : Call<UiNamesResponse>
}