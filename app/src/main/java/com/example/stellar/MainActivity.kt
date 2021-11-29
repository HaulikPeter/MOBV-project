package com.example.stellar

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.stellar.data.database.StellarDatabase
import com.example.stellar.data.database.StellarDatabaseRepository
import com.example.stellar.databinding.ActivityMainBinding
import com.example.stellar.ui.contacts.edit.ContactEditActivity
import com.example.stellar.ui.login.LoginActivity
import com.example.stellar.ui.login.LoginRepository
import com.example.stellar.ui.transaction.NewTransactionActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginRepository: LoginRepository
    private lateinit var db: StellarDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        loginRepository = LoginRepository.getInstance()
        db = StellarDatabase.db(this)

        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        val navigationView = binding.navView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_transactions, R.id.nav_contacts
        )
            .setOpenableLayout(binding.drawerLayout)
            .build()

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
        setupActionBarWithNavController(this, navController, mAppBarConfiguration)
        setupWithNavController(navigationView, navController)

        //podmienka dopisanana +
        binding.appBarMain.fab.setOnClickListener {
            val intent: Intent = if (navController.currentDestination?.id == R.id.nav_contacts) {
                Intent(baseContext, ContactEditActivity::class.java)
            } else {
                Intent(baseContext, NewTransactionActivity::class.java)
            }
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            askToDeleteContactsAndLogout()
            true
        }

        return true
    }

    private fun askToDeleteContactsAndLogout() {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> { lifecycleScope.launch {
                    db.contactsDao().clear()
                    db.transactionsDao().clear()
                    loginRepository.logout()
                    StellarDatabaseRepository(db.dao()).logout()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }}
                DialogInterface.BUTTON_NEUTRAL -> { return@OnClickListener }
                DialogInterface.BUTTON_NEGATIVE -> { lifecycleScope.launch {
                    db.transactionsDao().clear()
                    loginRepository.logout()
                    StellarDatabaseRepository(db.dao()).logout()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }}
            }
        }

        val builder = AlertDialog.Builder(this)
        builder
            .setMessage("Do you wish to delete the contact list as well?")
            .setPositiveButton("Yes", listener)
            .setNeutralButton("Cancel", listener)
            .setNegativeButton("No", listener)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp())
    }
}