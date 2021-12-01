package com.example.stellar.ui.contacts.edit

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.stellar.R
import com.example.stellar.data.database.ContactDatabaseDao
import com.example.stellar.data.database.StellarDatabase
import com.example.stellar.databinding.ContactEditActivityBinding
import com.example.stellar.databinding.ContactEditFragmentBinding
import com.example.stellar.ui.contacts.ContactsViewModel
import com.example.stellar.ui.contacts.ContactsViewModelFactory
import com.example.stellar.ui.login.LoginRepository
import org.stellar.sdk.*

/*
* Interaction with fragments is done through FragmentManager,
* which can be obtained via Activity.getFragmentManager() and Fragment.getFragmentManager().
*/
class ContactEditFragment : Fragment() {

    companion object {
        fun newInstance() = ContactEditFragment()
    }

    private lateinit var viewModel: ContactEditViewModel
    private lateinit var binding: ContactEditFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactEditFragmentBinding.inflate(inflater, container, false)
        val database: ContactDatabaseDao = StellarDatabase.db(this.requireContext()).contactsDao()
        viewModel = ViewModelProvider(this, ContactEditViewModelFactory(database)).get(ContactEditViewModel::class.java)

        val publicKey = requireActivity().intent.extras?.getString("publicKey")
        val nameText = binding.contactEditName
        val publicKeyText = binding.contactEditPublicKey
        val btn = binding.contactEditBtn

        /*
         * Condition of calling the correct one FloatingActionButton
         */
        Thread {
            viewModel.loadContact(publicKey)
            if (viewModel.contact?.name?.length ?: 0 > 0) {
                nameText.setText(viewModel.contact?.name)
            }
            if (viewModel.contact?.publicKey?.length ?: 0 > 0) {
                publicKeyText.setText(viewModel.contact?.publicKey)
            }
        }.start()

        //method that is called when the view (component) is clicked.
        btn.setOnClickListener {
            Thread {
                viewModel.contact!!.name = nameText.text.toString()
                viewModel.contact!!.publicKey = publicKeyText.text.toString()
                viewModel.saveContact()
                activity?.finish()
            }.start()
        }

        return binding.root
    }
}