package com.example.components.architecture.nvice.data.preference

import android.content.Context
import com.google.gson.Gson

abstract class BasePreference<T> constructor(
        context: Context?,
        preferenceRef: String?,
        private val cacheRef: String?,
        private val classOfT: Class<T>
) {
    private var sharedPreferences = context?.getSharedPreferences(
            preferenceRef, Context.MODE_PRIVATE
    )

    open fun get(): T? = pref()?.let { it }

    open fun set(value: T) {
        with(sharedPreferences?.edit()) {
            this?.putString(cacheRef, Gson().toJson(value))
            this?.apply()
        }
    }

    open fun clear() {
        with(sharedPreferences?.edit()) {
            this?.putString(cacheRef, null)
            this?.apply()
        }
    }

    open fun has() = pref() != null

    private fun pref(): T? = Gson().fromJson(sharedPreferences?.getString(cacheRef, null), classOfT)
}