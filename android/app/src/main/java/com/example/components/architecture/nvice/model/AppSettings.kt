package com.example.components.architecture.nvice.model

import org.parceler.Parcel

@Parcel
data class AppSettings(
        var language: String? = "en",
        var notification: Boolean? = true,
        var location: Boolean? = true,
        var isFirstOpenApp: Boolean? = true
)