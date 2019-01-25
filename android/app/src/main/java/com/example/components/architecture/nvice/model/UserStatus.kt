package com.example.components.architecture.nvice.model

import com.example.components.architecture.nvice.R
import com.example.components.architecture.nvice.util.random
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.StringUtils

enum class UserStatus constructor(val id: Int?) {

    @SerializedName("0")
    PERMANENT(0),

    @SerializedName("1")
    TEMPORARY(1),

    @SerializedName("2")
    UNDEFINED(2);

    fun getValue(): Int? = id

    fun getCapitalizedName(): String = StringUtils.capitalize(this.name.toLowerCase())

    fun getColorResource(): Int = when (this) {
        PERMANENT -> R.color.primary
        TEMPORARY -> R.color.primary_dark
        UNDEFINED -> R.color.black_500
    }

    companion object {
        fun from(id: Int?): UserStatus = requireNotNull(values().find { it.id == id })
        fun random(): UserStatus = values()[(0 until UserStatus.values().size).random()]
    }
}