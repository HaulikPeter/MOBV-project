package com.example.stellar.data.database

import androidx.annotation.WorkerThread
import com.example.stellar.data.database.entities.ContactEntity
import com.example.stellar.data.database.entities.Transaction
import com.example.stellar.data.database.entities.UserEntity

/**
 * The `StellarDatabaseRepository` class is a repository for database maintenance.
 */
class StellarDatabaseRepository {

    constructor(userDao: UserDatabaseDao) {
        this.userDao = userDao
    }

    constructor(contactDao: ContactDatabaseDao) {
        this.contactDao = contactDao
    }

    constructor(transactionDao: TransactionDatabaseDao) {
        this.transactionDao = transactionDao
    }

    constructor(userDao: UserDatabaseDao?, contactDao: ContactDatabaseDao?,
                transactionDao: TransactionDatabaseDao?) {
        this.userDao = userDao
        this.contactDao = contactDao
        this.transactionDao = transactionDao
    }

    var userDao: UserDatabaseDao? = null
    var contactDao: ContactDatabaseDao? = null
    var transactionDao: TransactionDatabaseDao? = null

    @WorkerThread
    suspend fun insertUser(user: UserEntity) = userDao?.insert(user)

    suspend fun updateUser(user: UserEntity) = userDao?.update(user)

    suspend fun getUsers(): List<UserEntity>? = userDao?.getUser()

    suspend fun logout() = userDao?.logout()

    @WorkerThread
    suspend fun insertContact(contact: ContactEntity) = contactDao?.insert(contact)

    fun updateContact(contact: ContactEntity) = contactDao?.update(contact)

    fun findContactByPublicKey(publicKey: String) = contactDao?.findByPublicKey(publicKey)

    fun deleteContact(publicKey: String) = contactDao?.delete(publicKey)

    @WorkerThread
    suspend fun insertTransaction(transaction: Transaction) = transactionDao?.insert(transaction)

    suspend fun getAllTransactions() = transactionDao?.getAllTransactions()

    suspend fun clearTransactions() = transactionDao?.clear()
}