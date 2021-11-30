package com.example.stellar.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stellar.data.database.entities.Transaction

/**
 * The `TransactionDatabaseDao` interface is generated data access object that manages the transactions table.
 */
@Dao
interface TransactionDatabaseDao {

     /**
      * Inserts a [transaction] entity to the database with replace conflict strategy if its primary
      * keys are the same.
      */
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(transaction: Transaction)

     /**
      * Returns all the [Transaction]s from the database in a mutable list.
      */
     @Query("SELECT * FROM transactions_table")
     suspend fun getAllTransactions(): MutableList<Transaction>

     /**
      * Wipes the whole transactions table data from the database.
      */
     @Query("DELETE FROM transactions_table")
     suspend fun clear()

}