package com.example.stellar.ui.transaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stellar.R

class NewTransactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_transaction)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.newTransactionContainer, NewTransactionFragment())
                .commitNow()
        }
    }
}