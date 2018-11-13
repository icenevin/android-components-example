package com.example.components.architecture.nvice.api.response

import com.google.gson.annotations.SerializedName

data class UiNamesResponse(
        @SerializedName("name")
        val name: String,

        @SerializedName("surname")
        val surname: String
)
