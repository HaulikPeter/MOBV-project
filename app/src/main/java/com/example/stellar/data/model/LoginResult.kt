package com.example.stellar.data.model

/**
 * Data class that stores the state of the login
 */
data class LoginResult(
    val successful: Boolean,
    val message: Int? = null
)