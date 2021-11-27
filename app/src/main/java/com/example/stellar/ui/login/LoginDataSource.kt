package com.example.stellar.ui.login

import com.example.stellar.data.Result
import com.example.stellar.data.model.LoggedInUser
import org.stellar.sdk.KeyPair
import java.io.IOException

class LoginDataSource {
    fun login(secretSeed: CharArray): Result<LoggedInUser> {
        return try {
            val fakeUser = LoggedInUser(KeyPair.fromSecretSeed(secretSeed))
            Result.Success(fakeUser)
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }
}