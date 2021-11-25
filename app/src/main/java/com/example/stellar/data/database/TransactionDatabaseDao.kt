package com.example.stellar.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stellar.ui.transactionList.Transaction

@Dao
interface TransactionDatabaseDao {

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insert(transaction: Transaction)

     @Query("SELECT * FROM transactions_table")
     suspend fun getAllTransactions(): List<Transaction>

     @Query("DELETE FROM transactions_table")
     suspend fun clear()

}