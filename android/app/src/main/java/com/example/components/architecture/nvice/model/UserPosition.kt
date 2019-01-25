package com.example.components.architecture.nvice.model

import com.example.components.architecture.nvice.util.random
import com.google.gson.annotations.SerializedName
import org.apache.commons.lang3.StringUtils

enum class UserPosition constructor(val id: Int?, val positionName: String, val positionGroup: String, val positionSubGroup: String) {

    @SerializedName("0")
    SUPER_ADMIN(0, "Super Admin", "Super Admin", "Super Admin"),

    @SerializedName("1")
    CEO(1, "Chief Executive Officer", "CEO", "CEO"),

    @SerializedName("100")
    DEV(100, "Developer", "Developer", "Developer"),
    @SerializedName("101")
    DEV_FRONTEND(101, "Front-end Developer", "Developer", "Developer"),
    @SerializedName("102")
    DEV_BACKEND(102, "Back-end Developer", "Developer", "Developer"),
    @SerializedName("103")
    DEV_MOBILE(103, "Mobile Developer", "Developer", "Mobile Developer"),
    @SerializedName("104")
    DEV_MOBILE_ANDROID(104, "Android Developer", "Developer", "Mobile Developer"),
    @SerializedName("105")
    DEV_MOBILE_IOS(105, "iOS Developer", "Developer", "Mobile Developer"),

    @SerializedName("9999")
    UNDEFINED(9999, "Undefined", "Undefined", "Undefined");

    fun getValue(): Int? = id

    fun getCapitalizedName(): String = StringUtils.capitalize(this.positionGroup)

    companion object {
        fun from(id: Int?): UserPosition = requireNotNull(values().find { it.id == id })
        fun random(): UserPosition = values()[(0 until UserPosition.values().size).random()]
    }
}