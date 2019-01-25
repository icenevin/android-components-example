package com.example.components.architecture.nvice.data.converter

import android.arch.persistence.room.TypeConverter
import com.example.components.architecture.nvice.model.UserPosition

class UserPositionConverter {
    @TypeConverter
    fun toStatus(value: Int?): UserPosition? = UserPosition.from(value)

    @TypeConverter
    fun toValue(value: UserPosition?): Int? = value?.getValue()
}