package com.example.components.architecture.nvice.data.converter

import androidx.room.TypeConverter
import com.example.components.architecture.nvice.model.user.UserExperience
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


class UserExperienceConverter {

    @TypeConverter
    fun toList(value: String?): List<UserExperience>? = Gson().fromJson(value, object : TypeToken<List<UserExperience>>() {}.type)

    @TypeConverter
    fun toString(list: List<UserExperience>?): String? = Gson().toJson(list, object : TypeToken<List<UserExperience>>() {}.type)
}