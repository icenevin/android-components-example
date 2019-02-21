package com.example.components.architecture.nvice.api.service

import com.example.components.architecture.nvice.api.response.UiFacesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UiFacesService {
    @GET("api")
    fun getAvatars() : Call<List<UiFacesResponse>>

    @GET("api")
    fun getAvatarsByProvider(
            @Query("provider%5B%5D") provider: String,
            @Query("limit") limit: String,
            @Query("random") random: String
    ) : Deferred<List<UiFacesResponse>>
}