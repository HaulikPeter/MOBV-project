package com.example.stellar.ui.contacts.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stellar.data.database.ContactDatabaseDao
import com.example.stellar.data.database.entities.ContactEntity
import kotlinx.coroutines.launch

class ContactEditViewModel(private val contactDatabaseDao: ContactDatabaseDao) : ViewModel() {
    var contact: ContactEntity? = null

    //Load contacts
    fun loadContact(publicKey: String?) {
        //If we create new contact
        if (publicKey == null) {

            contact = ContactEntity("", "")
        } else {
            // If we edit existing contact
            contact = contactDatabaseDao.findByPublicKey(publicKey)
        }
    }

    // Save contact
    fun saveContact() {
        viewModelScope.launch { contact?.let { contactDatabaseDao.insert(it) } }
    }
}