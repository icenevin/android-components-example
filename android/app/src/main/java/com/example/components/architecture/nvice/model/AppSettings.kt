package com.example.components.architecture.nvice.model

import org.parceler.Parcel

@Parcel
data class AppSettings(
        var language: String? = "th",
        var notification: Boolean? = true,
        var location: Boolean? = true,
        var isFirstOpenApp: Boolean? = true
)