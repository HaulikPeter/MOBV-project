package com.example.stellar.ui.contacts.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stellar.data.database.ContactDatabaseDao

class ContactEditViewModelFactory(private val contactDatabaseDao: ContactDatabaseDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ContactEditViewModel::class.java)) {
            ContactEditViewModel(contactDatabaseDao) as T
        } else {
            throw IllegalArgumentException("")
        }
    }
}