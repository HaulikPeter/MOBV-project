package com.example.stellar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavGraph
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.stellar.databinding.ActivityMainBinding
import com.example.stellar.ui.login.LoginActivity
import com.example.stellar.ui.login.LoginRepository
import com.example.stellar.ui.login.LoginViewModel
import com.example.stellar.ui.transactions.TransactionActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginRepository: LoginRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginRepository = LoginRepository.getInstance()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        //setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {
            val intent = Intent(baseContext, TransactionActivity::class.java)
            startActivity(intent)
        }

        val navigationView = binding.navView
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_contacts, R.id.nav_transactions
        )
            .setOpenableLayout(binding.drawerLayout)
            .build()

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration)
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        menu?.getItem(0)?.setOnMenuItemClickListener {
            loginRepository.logout()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
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