package com.example.stellar.data.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.stellar.data.database.entities.UserEntity

class StellarDatabaseRepository(private val dao: StellarDatabaseDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: UserEntity) {
        dao.insert(user)
    }

    suspend fun update(user: UserEntity) = dao.update(user)

    suspend fun users(): List<UserEntity> = dao.getUser()

    suspend fun logout() = dao.logout()
}