package com.example.stellar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stellar.data.database.entities.ContactEntity
import com.example.stellar.data.database.entities.UserEntity
import com.example.stellar.data.database.entities.Transaction

/**
 *  The `StellarDatabase` class defines the abstract layer of the database.
 */
@Database(entities = [
    UserEntity::class,
    ContactEntity::class,
    Transaction::class], version = 2, exportSchema = false)
abstract class StellarDatabase : RoomDatabase() {

    abstract fun usersDao(): UserDatabaseDao
    abstract fun contactsDao(): ContactDatabaseDao
    abstract fun transactionsDao(): TransactionDatabaseDao

    /**
     * Creates a singleton class which can be accessed by calling the [db] function.
     */
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