package com.example.stellar.ui.contacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stellar.data.database.ContactDatabaseDao
import java.lang.IllegalArgumentException

class ContactsViewModelFactory(private val contactDatabaseDao: ContactDatabaseDao): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            ContactsViewModel(contactDatabaseDao) as T
        } else {
            throw IllegalArgumentException("")
        }
    }
}