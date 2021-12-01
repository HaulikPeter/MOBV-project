package com.example.stellar.ui.contacts

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stellar.R
import com.example.stellar.data.database.ContactDatabaseDao
import com.example.stellar.data.database.StellarDatabase
import com.example.stellar.data.database.entities.ContactEntity
import com.example.stellar.databinding.ContactViewHolderBinding
import com.example.stellar.databinding.FragmentContactsBinding
import com.example.stellar.ui.contacts.edit.ContactEditActivity
import com.example.stellar.ui.transaction.NewTransactionActivity

class ContactsFragment : Fragment() {

    private lateinit var viewModel: ContactsViewModel
    private lateinit var binding: FragmentContactsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val database: ContactDatabaseDao = StellarDatabase.db(this.requireContext()).contactsDao()
        viewModel = ViewModelProvider(this, ContactsViewModelFactory(database)).get(ContactsViewModel::class.java)

        val adapter = ContactsAdapter(
            onEdit = { contact: ContactEntity ->
                val intent = Intent(requireActivity().baseContext, ContactEditActivity::class.java)
                intent.putExtra("publicKey", contact.publicKey)
                startActivity(intent)
            },
            onDelete = { contact: ContactEntity ->
                Thread {
                    database.delete(contact.publicKey)
                }.start()
            },
            onTransaction = { contact: ContactEntity ->
                val intent = Intent(requireActivity().baseContext, NewTransactionActivity::class.java)
                intent.putExtra("publicKey", contact.publicKey)
                startActivity(intent)
            }
        )
        binding = FragmentContactsBinding.inflate(inflater, container, false)
        binding.contactsList.adapter = adapter

        // Linking database with contact list in Recycler View
        viewModel.getContacts().observe(viewLifecycleOwner, { contacts: MutableList<ContactEntity> ->
            adapter.setContactList(contacts)
        })

        return binding.root
    }
}