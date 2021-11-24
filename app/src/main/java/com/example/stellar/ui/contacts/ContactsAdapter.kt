package com.example.stellar.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.stellar.R
import com.example.stellar.data.database.entities.ContactEntity
import java.util.*

/**
 * https://developer.android.com/codelabs/kotlin-android-training-recyclerview-fundamentals#2
 */
class ContactsAdapter(
    val onDelete: (contact: ContactEntity) -> Unit,
    val onEdit: (contact: ContactEntity) -> Unit,
    val onTransaction: (contact: ContactEntity) -> Unit
) : RecyclerView.Adapter<ContactViewHolder>() {

    private var contactList: MutableList<ContactEntity>

    fun setContactList(contactList: MutableList<ContactEntity>) {
        this.contactList = contactList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ContactViewHolder(inflater.inflate(R.layout.contact_view_holder, parent, false))
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        val nameText = holder.itemView.findViewById<TextView>(R.id.contact_name_text)
        val publicKeyText = holder.itemView.findViewById<TextView>(R.id.contact_publicKey_text)

        nameText.text = contact.name
        publicKeyText.text = contact.publicKey

        val deleteBtn = holder.itemView.findViewById<AppCompatImageButton>(R.id.contact_btn_delete)
        val editBtn = holder.itemView.findViewById<AppCompatImageButton>(R.id.contact_btn_edit)
        val transactionBtn = holder.itemView.findViewById<AppCompatImageButton>(R.id.contact_btn_transaction)
        deleteBtn.setOnClickListener {
            onDelete(contact)
        }
        editBtn.setOnClickListener {
            onEdit(contact)
        }
        transactionBtn.setOnClickListener {
            onTransaction(contact)
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    init {
        contactList = LinkedList()
    }
}