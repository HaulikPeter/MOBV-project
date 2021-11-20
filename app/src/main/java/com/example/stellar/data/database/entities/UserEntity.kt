package com.example.stellar.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for the structure of the user table of the currently logged in user.
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