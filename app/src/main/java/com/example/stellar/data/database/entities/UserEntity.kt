package com.example.stellar.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The `UserEntity` class represents an entity for the user table used by the database.
 */
@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    var privateKey: String,

    @ColumnInfo(name = "public_key")
    var publicKey: String,

    @ColumnInfo(name = "balance")
    var balance: String?
)