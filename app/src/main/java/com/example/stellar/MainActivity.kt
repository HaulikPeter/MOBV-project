package com.example.stellar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.stellar.data.database.StellarDatabase
import com.example.stellar.data.database.StellarDatabaseRepository
import com.example.stellar.data.database.entities.UserEntity
import com.example.stellar.databinding.ActivityMainBinding
import com.example.stellar.ui.login.LoginActivity
import com.example.stellar.ui.login.LoginRepository
import com.example.stellar.ui.transaction.NewTransactionActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginRepository: LoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginRepository = LoginRepository.getInstance()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            val intent = Intent(baseContext, NewTransactionActivity::class.java)
            startActivity(intent)
        }

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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            loginRepository.logout()

            lifecycleScope.launch {
                val repo = StellarDatabaseRepository(StellarDatabase.db(this@MainActivity).dao())
                repo.logout()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            true
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp())
    }
}