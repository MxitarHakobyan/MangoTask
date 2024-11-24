package com.mango.task.data.localStorage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mango.task.data.model.local.ProfileEntity

@Database(entities = [ProfileEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}