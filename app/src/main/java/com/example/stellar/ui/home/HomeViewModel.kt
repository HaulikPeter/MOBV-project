package com.example.stellar.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stellar.data.model.LoggedInUser
import com.example.stellar.ui.login.LoginRepository

class HomeViewModel : ViewModel() {

    private val userData: MutableLiveData<LoggedInUser?>?
    private val loginRepository: LoginRepository?

    init {
        loginRepository = LoginRepository.getInstance()
        userData = MutableLiveData()
        userData.value = loginRepository.user
    }

    fun getUser(): LiveData<LoggedInUser?>? = userData
}