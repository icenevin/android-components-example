package com.example.components.architecture.nvice.data.preference

import android.content.Context
import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.model.AppSettings
import com.google.gson.Gson


class AppSettingsPreference(var context: Context?) {

    private var sharedPreferences = context?.getSharedPreferences(
            context?.getString(R.string.preference_settings), Context.MODE_PRIVATE)

    private var appSettings: AppSettings? = Gson().fromJson(sharedPreferences?.getString(context?.getString(R.string.cache_app_settings), null), AppSettings::class.java)

    fun getSettings(): AppSettings? {
        return appSettings?.let { it } ?: AppSettings()
    }

    fun setSettings(appSettings: AppSettings?) {
        this.appSettings = appSettings?.copy()
        with(sharedPreferences?.edit()) {
            this!!.putString(context?.getString(R.string.cache_app_settings), Gson().toJson(appSettings))
            apply()
        }
    }

    fun clearSettings() {
        appSettings = null
        with(sharedPreferences?.edit()) {
            this!!.putString(context?.getString(R.string.cache_app_settings), null)
            apply()
        }
    }

    fun hasSettings(): Boolean {
        return appSettings != null
    }

    interface OnLanguageSettingsChangeListener {
        fun onLanguageChanged()
    }
}