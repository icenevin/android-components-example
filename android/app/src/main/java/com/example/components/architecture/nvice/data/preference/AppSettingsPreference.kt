package com.example.components.architecture.nvice.data.preference

import android.content.Context
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.model.AppSettings
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsPreference @Inject constructor(context: Context?) : BasePreference<AppSettings>(
        context,
        "preference_settings",
        "cache_app_settings",
        AppSettings::class.java
) {
    override fun get() = super.get() ?: AppSettings()
}