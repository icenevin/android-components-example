package com.example.components.architecture.nvice.data.converter

import androidx.room.TypeConverter
import com.example.components.architecture.nvice.model.user.UserSkill
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


class UserSkillsConverter {

    @TypeConverter
    fun toList(value: String?): List<UserSkill>? = Gson().fromJson(value, object : TypeToken<List<UserSkill>>() {}.type)

    @TypeConverter
    fun toString(list: List<UserSkill>?): String? = Gson().toJson(list, object : TypeToken<List<UserSkill>>() {}.type)
}