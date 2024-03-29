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
import com.example.stellar.ui.auth.addKey
import com.example.stellar.ui.auth.encrypt
import kotlinx.coroutines.launch
import org.stellar.sdk.KeyPair

/**
 * Class for login and sign up
 */
class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    /**
     * The function gets secret seed and pin and identifies the user
     * from the database
     * The login can be successfull or failed
     */
    fun login(secretSeed: String, pin: String, context: Context) {
        val result = loginRepository.login(secretSeed.toCharArray())
        if (result is Result.Success) {

            viewModelScope.launch {
                val dbRepo = StellarDatabaseRepository(StellarDatabase.db(context).usersDao())

                val accountId = KeyPair.fromSecretSeed(secretSeed.toCharArray()).accountId
                val encryptedPrivateKey = secretSeed.encrypt(pin.addKey())

                val user = UserEntity(encryptedPrivateKey, accountId, null)
                dbRepo.insertUser(user)
            }

            _loginResult.value =
                LoginResult(message = R.string.login_successful, successful = true)
        } else {
            _loginResult.value =
                LoginResult(message = R.string.login_failed, successful = false)
        }
    }

    /**
     * The function gets a generated pin by the user
     * It runs on the test network, we get 10,000
     * testXLM from Friendbot, which is a friendly account funding tool
     */
    fun signUp(context: Context, pin: String) {
        val keyPair = KeyPair.random()
        // https://developer.android.com/training/volley/simple#kotlin
        val queue = Volley.newRequestQueue(context)
        val url = "https://horizon-testnet.stellar.org/friendbot?addr=${keyPair.accountId}"

        val friendBotRequest = StringRequest(
            Request.Method.GET,
            url,
            { login(keyPair.secretSeed.concatToString(), pin, context) },
            { error -> println(error) }
        )
        friendBotRequest.retryPolicy = DefaultRetryPolicy(
            50_000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queue.add(friendBotRequest)
    }
}