package com.example.stellar.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for the structure of the contacts table.
 */
@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey(autoGenerate = false)
    var publicKey: String,
    var name: String
)