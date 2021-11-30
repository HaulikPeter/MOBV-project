package com.example.stellar.ui.contacts.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stellar.data.database.ContactDatabaseDao
import com.example.stellar.data.database.entities.ContactEntity
import kotlinx.coroutines.launch

class ContactEditViewModel(private val contactDatabaseDao: ContactDatabaseDao) : ViewModel() {
    var contact: ContactEntity? = null

    fun loadContact(publicKey: String?) {
        if (publicKey == null) {
            // ak vytvarame novy kontakt
            contact = ContactEntity("", "")
        } else {
            // ak editujeme existujuci kontakt
            contact = contactDatabaseDao.findByPublicKey(publicKey)
        }
    }

    fun saveContact() {
        viewModelScope.launch { contact?.let { contactDatabaseDao.insert(it) } }
    }
}