package com.example.stellar.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.stellar.data.database.entities.ContactEntity

/**
 * The `ContactDatabaseDao` interface is a generated data access object that manages the contacts table.
 */
@Dao
interface ContactDatabaseDao {

    /**
     * Inserts a [contact] entity to the database with replace conflict strategy if its primary
     * keys are the same.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: ContactEntity)

    /**
     * Updates the [contact] entity in the database with replace conflict strategy if its primary
     * keys are the same.
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(contact: ContactEntity)

    /**
     * Queries the database using the [publicKey] and returns its corresponding [ContactEntity] if
     * present.
     */
    @Query("SELECT * from contacts_table WHERE publicKey = :publicKey")
    fun findByPublicKey(publicKey: String): ContactEntity?

    /**
     * Removes a [ContactEntity] from the database where the [publicKey]s match.
     */
    @Query("DELETE FROM contacts_table WHERE publicKey = :publicKey")
    fun delete(publicKey: String)

    /**
     * Wipes the whole contacts table from the database.
     */
    @Query("DELETE FROM contacts_table")
    suspend fun clear()

    /**
     * Returns all the [ContactEntity] values stored in the database as livedata in a list.
     */
    @Query("SELECT * FROM contacts_table")
    fun getAllContacts(): LiveData<MutableList<ContactEntity>>
}