package com.example.components.architecture.nvice.util

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import com.example.components.architecture.nvice.data.preference.AppSettingsPreference
import java.util.*

object LocaleHelper {

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context): String {
        return getPersistedData(context)
    }

    fun setLocale(context: Context, language: String): Context {
        persist(context, language)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)

    }

    private fun getPersistedData(context: Context): String {
        val appSettingsPreference = AppSettingsPreference(context)
        return appSettingsPreference.get().language!!
    }

    private fun persist(context: Context, language: String) {
        val appSettingsPreference = AppSettingsPreference(context)
        appSettingsPreference.get().language = language
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {

        val locale = Locale(language)
        Locale.setDefault(locale)

        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, language: String): Context {
        val locale = Locale(language)
        val resources = context.resources
        val configuration = resources.configuration

        Locale.setDefault(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
        return context
    }
}

