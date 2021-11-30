package com.example.stellar.data.model

import org.stellar.sdk.KeyPair

/**
 * Class that used for login mechanism
 */
class LoggedInUser(private val keyPair: KeyPair) {
    fun getAccountId(): String = keyPair.accountId
}