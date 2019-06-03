package com.example.components.architecture.nvice.data.converter

import androidx.room.TypeConverter
import com.example.components.architecture.nvice.model.user.UserStatus

class UserStatusConverter {
    @TypeConverter
    fun toStatus(value: Int?): UserStatus? = UserStatus.from(value)

    @TypeConverter
    fun toValue(value: UserStatus?): Int? = value?.getValue()
}