package com.example.stellar.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.stellar.data.database.entities.ContactEntity

/**
 * Data access object for contacts table
 */
@Dao
interface ContactDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: ContactEntity)

    /**
     * livedata - auto update
     */
    @Query("SELECT * FROM contacts")
    fun findAll(): LiveData<MutableList<ContactEntity>>

    @Query("SELECT * from contacts WHERE publicKey = :publicKey")
    fun findByPublicKey(publicKey: String): ContactEntity?

    @Query("DELETE FROM contacts WHERE publicKey = :publicKey")
    fun delete(publicKey: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(contact: ContactEntity)
}