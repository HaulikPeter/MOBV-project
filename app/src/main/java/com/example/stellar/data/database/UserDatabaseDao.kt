package com.example.stellar.data.database

import androidx.room.*
import com.example.stellar.data.database.entities.UserEntity

/**
 * Data access object for the user table
 */

@Dao
interface UserDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM user_table")
    suspend fun getUser(): List<UserEntity>

    @Query("DELETE FROM user_table")
    suspend fun logout()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: UserEntity)
}