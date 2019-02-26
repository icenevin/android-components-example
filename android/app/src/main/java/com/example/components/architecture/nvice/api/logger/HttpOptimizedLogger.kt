package com.example.components.architecture.nvice.api.logger

import com.google.gson.JsonSyntaxException
import com.google.gson.JsonParser
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class HttpOptimizedLogger : HttpLoggingInterceptor.Logger {

    override fun log(message: String) {
        if (!message.startsWith("{")) {
            Timber.d(message)
            return
        }
        try {
            val prettyPrintJson = GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(JsonParser().parse(message))
            Timber.d(prettyPrintJson)
        } catch (e: JsonSyntaxException) {
            Timber.e("JsonSyntaxException: ${e.message}")
        } catch (e: Exception) {
            Timber.e("Exception: ${e.message}")
        }
    }
}