package com.example.components.architecture.nvice.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.example.components.architecture.nvice.data.converter.UserStatusConverter
import com.example.components.architecture.nvice.dao.UserDao
import com.example.components.architecture.nvice.model.User

@Database(
        entities = [
            User::class
        ],
        version = 1
)
@TypeConverters(UserStatusConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}