package com.example.doyourself.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.doyourself.data.local.entities.*

@Database(
    entities = [
        ProcedureDraftEntity::class,
        StepEntity::class,
        BlockEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun procedureDao(): ProcedureDao
}
