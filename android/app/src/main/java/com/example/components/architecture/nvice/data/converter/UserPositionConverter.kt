package com.example.components.architecture.nvice.data.converter

import androidx.room.TypeConverter
import com.example.components.architecture.nvice.model.user.UserPosition

class UserPositionConverter {
    @TypeConverter
    fun toStatus(value: Int?): UserPosition? = UserPosition.from(value)

    @TypeConverter
    fun toValue(value: UserPosition?): Int? = value?.getValue()
}