package com.example.stellar.data.model

import org.stellar.sdk.KeyPair

//toto
class LoggedInUser(private val keyPair: KeyPair) {
    fun getAccountId(): String = keyPair.accountId

    fun getSecretSeed(): String = String(keyPair.secretSeed)

    fun getKeyPair(): KeyPair = keyPair
}