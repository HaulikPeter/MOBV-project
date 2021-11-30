package com.example.stellar.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.stellar.MainActivity
import com.example.stellar.data.database.StellarDatabase
import com.example.stellar.data.database.StellarDatabaseRepository
import com.example.stellar.databinding.ActivityLoginBinding
import com.example.stellar.ui.auth.PromptPinDialogFragment
import com.example.stellar.ui.auth.addKey
import com.example.stellar.ui.auth.decrypt
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: StellarDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = StellarDatabase.db(this)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]

        val etUsername = binding.etUsername
        val btnLogin = binding.btnLogin
        val btnSignup = binding.btnSignup

        val intent = Intent(this, MainActivity::class.java)

        val repo = StellarDatabaseRepository(StellarDatabase.db(this).usersDao())
        lifecycleScope.launch {
            val user = repo.getUsers()
            if (user.isNullOrEmpty())
                return@launch

//            LoginRepository.getInstance().login(user[0].privateKey.toCharArray())
            val fragment = PromptPinDialogFragment( { pin ->
//                loginViewModel.login(user[0].privateKey, pin, this@LoginActivity)
                try {
                    LoginRepository.getInstance()
                        .login(user[0].privateKey.decrypt(pin.addKey()).toCharArray())
                    startActivity(intent)
                    setResult(RESULT_OK)
                    finish()
                }
                catch(e: Exception){
                    lifecycleScope.launch {
                        db.contactsDao().clear()
                        db.transactionsDao().clear()
                        LoginRepository.getInstance().logout()
                        StellarDatabaseRepository(db.usersDao()).logout()
                    }
                    Snackbar.make(
                        binding.root, "Wrong pin! Database cleared!",
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                }
            }, "Caution! Wrong password causes logout!")
            fragment.isCancelable = false
            fragment.show(supportFragmentManager, "PromptPinDialogFragment")
        }

        loginViewModel.loginResult.observe(this, {
            val loginResult = it
            if (loginResult.message != null) {
                if (loginResult.successful) {
                    startActivity(intent)
                    setResult(RESULT_OK)
                    finish()
                } else {
                    println(loginResult.message)
                    Snackbar.make(
                        binding.root, loginResult.message,
                        BaseTransientBottomBar.LENGTH_SHORT
                    ).show()
                }
            }
        })

        btnLogin.setOnClickListener {
            val fragment = PromptPinDialogFragment { pin ->
                loginViewModel.login(etUsername.text.toString(), pin, this)
            }
            fragment.show(supportFragmentManager, "PromptPinDialogFragment")
        }
        btnSignup.setOnClickListener {
            val fragment = PromptPinDialogFragment { pin ->
                loginViewModel.signUp(baseContext, pin)
            }
            fragment.show(supportFragmentManager, "PromptPinDialogFragment")
        }
    }
}