package com.example.hegemonycompose.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlayerEntity::class], version = 1, exportSchema = false)
abstract class PlayerDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
}