package com.example.components.architecture.nvice.data.converter

import android.arch.persistence.room.TypeConverter
import com.example.components.architecture.nvice.model.UserStatus

class UserStatusConverter {
    @TypeConverter
    fun toStatus(value: Int?): UserStatus? = UserStatus.from(value)

    @TypeConverter
    fun toValue(value: UserStatus?): Int? = value?.getValue()
}