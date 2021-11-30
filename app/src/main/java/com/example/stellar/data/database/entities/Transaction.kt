package com.example.stellar.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The `Transaction` class represents an entity for the transactions table used by the database.
 */
@Entity(tableName = "transactions_table")
data class Transaction(
    @PrimaryKey(autoGenerate = false)
    var uid: Long,

    @ColumnInfo(name = "source_address")
    var sourceAddress: String? = null,

    @ColumnInfo(name = "destination_address")
    var destinationAddress: String? = null,

    @ColumnInfo(name = "amount")
    var amount: String? = null,

    @ColumnInfo(name = "memo")
    var memo: String? = null,

    @ColumnInfo(name = "date")
    var date: String? = null,

    @ColumnInfo(name = "type")
    var type: Int? = null
)