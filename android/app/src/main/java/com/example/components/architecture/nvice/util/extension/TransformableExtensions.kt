package com.example.components.architecture.nvice.util.extension

import com.example.components.architecture.nvice.model.Transformable
import com.google.gson.Gson

fun <T> Transformable.transformTo(classOfT: Class<T>) : T? {
    val gson = Gson()
    val json = gson.toJson(this)
    return gson.fromJson(json, classOfT)
}