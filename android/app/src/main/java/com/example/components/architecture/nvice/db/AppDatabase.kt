package com.example.components.architecture.nvice.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.components.architecture.nvice.data.converter.UserStatusConverter
import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.data.converter.UserPositionConverter
import com.example.components.architecture.nvice.model.User

@Database(
        entities = [
            User::class
        ],
        version = 1
)
@TypeConverters(
        UserStatusConverter::class,
        UserPositionConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}