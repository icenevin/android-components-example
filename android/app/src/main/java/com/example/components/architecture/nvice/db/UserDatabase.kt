package com.example.components.architecture.nvice.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.components.architecture.nvice.data.converter.UserStatusConverter
import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.data.converter.UserExperienceConverter
import com.example.components.architecture.nvice.data.converter.UserSkillsConverter
import com.example.components.architecture.nvice.data.converter.UserPositionConverter
import com.example.components.architecture.nvice.model.user.User

@Database(
        entities = [
            User::class
        ],
        version = 1
)
@TypeConverters(
        UserSkillsConverter::class,
        UserStatusConverter::class,
        UserPositionConverter::class,
        UserExperienceConverter::class
)
abstract class UserDatabase : RoomDatabase() {
    companion object{
        const val NAME = "user.db"
    }
    abstract fun userDao(): UserDao
}