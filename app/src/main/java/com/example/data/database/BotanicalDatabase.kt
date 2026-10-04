package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.PlantEntity

@Database(entities = [PlantEntity::class], version = 1, exportSchema = false)
abstract class BotanicalDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao

    companion object {
        @Volatile
        private var INSTANCE: BotanicalDatabase? = null

        fun getDatabase(context: Context): BotanicalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BotanicalDatabase::class.java,
                    "botanical_assistant_database"
                )
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
