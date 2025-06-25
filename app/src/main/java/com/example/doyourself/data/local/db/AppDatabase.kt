package com.example.doyourself.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import android.content.Context
import com.example.doyourself.data.local.entities.*


@Database(
    entities = [
        ProcedureDraftEntity::class,
        StepEntity::class,
        BlockEntity::class,
        LikedProcedureEntity::class,
        LikedStepEntity::class,
        LikedBlockEntity::class,
        ProcedureProgressEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun procedureDao(): ProcedureDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "doyourself.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}


