package com.example.stellar.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stellar.MainActivity
import com.example.stellar.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val etUsername = binding.etUsername
        val btnLogin = binding.btnLogin
        val btnSignup = binding.btnSignup

        val intent = Intent(this, MainActivity::class.java)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())[LoginViewModel::class.java]
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
                        BaseTransientBottomBar.LENGTH_INDEFINITE
                    ).show()
                }
            }
        })

        btnLogin.setOnClickListener { loginViewModel.login(etUsername.text.toString()) }
        btnSignup?.setOnClickListener { loginViewModel.signUp(baseContext) }
    }

}