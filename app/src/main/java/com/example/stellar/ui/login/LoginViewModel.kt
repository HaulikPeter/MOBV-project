package com.example.stellar.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.stellar.R
import com.example.stellar.data.Result
import com.example.stellar.data.database.StellarDatabase
import com.example.stellar.data.database.StellarDatabaseRepository
import com.example.stellar.data.database.entities.UserEntity
import com.example.stellar.data.model.LoginResult
import kotlinx.coroutines.launch
import org.stellar.sdk.KeyPair

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(secretSeed: String, context: Context) {
        val result = loginRepository.login(secretSeed)
        if (result is Result.Success) {

            val dbRepo = StellarDatabaseRepository(StellarDatabase.db(context).dao())
            val keyPair = KeyPair.fromSecretSeed(secretSeed)
            val user = UserEntity(secretSeed, keyPair.accountId, null)
            viewModelScope.launch {
                dbRepo.insert(user)
            }

            _loginResult.value =
                LoginResult(message = R.string.login_successful, successful = true)
        } else {
            _loginResult.value =
                LoginResult(message = R.string.login_failed, successful = false)
        }
    }

    //Toto bolo pridane
    fun signUp(context: Context) {
        val keyPair = KeyPair.random()
        // https://developer.android.com/training/volley/simple#kotlin
        val queue = Volley.newRequestQueue(context)
        val url = "https://horizon-testnet.stellar.org/friendbot?addr=${keyPair.accountId}"

        val friendBotRequest = StringRequest(
            Request.Method.GET,
            url,
            { login(String(keyPair.secretSeed), context) },
            { error -> println(error) }
        )
        friendBotRequest.retryPolicy = DefaultRetryPolicy(
            50_000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queue.add(friendBotRequest)
    }
}