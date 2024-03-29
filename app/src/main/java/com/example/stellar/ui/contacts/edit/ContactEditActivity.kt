package com.example.stellar.ui.contacts.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stellar.R

/**
 * The Activity class defines the following call backs. Generated Android Studio.
 */
class ContactEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_edit_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ContactEditFragment.newInstance())
                .commitNow()
        }

    }
}