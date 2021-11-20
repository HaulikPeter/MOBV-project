package com.example.stellar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stellar.data.database.entities.UserEntity

/**
 *  Class that defines the abstract layer of the database
 */

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class StellarDatabase : RoomDatabase() {

    abstract fun dao(): StellarDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: StellarDatabase? = null

        fun db(context: Context): StellarDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        StellarDatabase::class.java,
                        "stellar_database.sqlite"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                instance
            }
        }
    }

}