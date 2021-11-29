package com.example.stellar.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stellar.data.database.entities.ContactEntity
import com.example.stellar.data.database.entities.UserEntity
import com.example.stellar.ui.transactionList.Transaction

/**
 *  Class that defines the abstract layer of the database
 */

@Database(entities = [
    UserEntity::class,
    ContactEntity::class,
    Transaction::class], version = 3, exportSchema = false)
abstract class StellarDatabase : RoomDatabase() {

    abstract fun dao(): StellarDatabaseDao
    abstract fun contactsDao(): ContactDatabaseDao
    abstract fun transactionsDao(): TransactionDatabaseDao

    // TODO: If user logs out, database must be ceased
    // TODO: When logout, ask user whether wishes to delete its contacts

    companion object {
        @Volatile
        private var INSTANCE: StellarDatabase? = null

        //inicializacia tabulky
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