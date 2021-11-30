package com.example.stellar.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The `ContactEntity` class represents an entity for the contacts table used by the database.
 */
@Entity(tableName = "contacts_table")
data class ContactEntity(
    @PrimaryKey(autoGenerate = false)
    var publicKey: String,

    @ColumnInfo(name = "name")
    var name: String
)