package com.example.stellar.ui.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.stellar.data.database.ContactDatabaseDao
import com.example.stellar.data.database.entities.ContactEntity

class ContactsViewModel(private val contactDatabaseDao: ContactDatabaseDao) : ViewModel() {
    /**
     * Aktualny zoznam kontaktov, nacitava ho ContactsFragment
     */
    private val contacts: LiveData<MutableList<ContactEntity>>

    fun getContacts(): LiveData<MutableList<ContactEntity>> {
        return contacts
    }

    init {
        contacts = contactDatabaseDao.findAll()
    }
}