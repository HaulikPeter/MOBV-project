package com.example.stellar.ui.login

import com.example.stellar.data.Result
import com.example.stellar.data.model.LoggedInUser

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class LoginRepository(private val dataSource: LoginDataSource) {

    var user: LoggedInUser? = null
        private set

    fun logout() {
        user = null
    }

    fun login(secretKey: CharArray): Result<LoggedInUser> {
        val result = dataSource.login(secretKey)
        if (result is Result.Success)
            setLoggedInUser(result.data)
        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginRepository? = null
        fun getInstance(): LoginRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = LoginRepository(LoginDataSource())
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}