package com.example.components.architecture.nvice.api.response

import com.google.gson.annotations.SerializedName

data class UnsplashPhotosResponse(
        @SerializedName("urls")
        val urls: Url = Url()
)

data class Url(
        @SerializedName("thumb")
        val thumb: String? = "",

        @SerializedName("regular")
        val regular: String = ""
)