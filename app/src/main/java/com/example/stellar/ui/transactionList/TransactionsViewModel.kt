package com.example.stellar.ui.transactionList

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.stellar.data.database.StellarDatabase
import com.example.stellar.data.model.LoggedInUser
import com.example.stellar.ui.login.LoginRepository
import kotlinx.coroutines.launch
import org.stellar.sdk.Server
import org.stellar.sdk.requests.EventListener
import org.stellar.sdk.responses.operations.OperationResponse
import org.stellar.sdk.responses.operations.PaymentOperationResponse
import shadow.com.google.common.base.Optional
import java.lang.Exception

class TransactionsViewModel(context: Context) : ViewModel() {

    private val userData: MutableLiveData<LoggedInUser?>?
    private val loginRepository: LoginRepository?
    val adapter: TransactionAdapter
    private val db: StellarDatabase
    private lateinit var dbTransactionList: List<Transaction>

    init {
        loginRepository = LoginRepository.getInstance()
        userData = MutableLiveData()
        userData.value = loginRepository.user
        adapter = TransactionAdapter()
        db = StellarDatabase.db(context)
        viewModelScope.launch {
            dbTransactionList = db.transactionsDao().getAllTransactions()
        }
    }

    fun loadDataFromServer(recyclerView: RecyclerView) {
        val server = Server("https://horizon-testnet.stellar.org")
        val userId = userData?.value


        val paymentRequest = server.payments().forAccount(userId?.getAccountId())
        paymentRequest.stream(object : EventListener<OperationResponse> {
            override fun onEvent(operation: OperationResponse?) {

                // TODO: Check if db contains instance of the transaction by its id, if so, skip

                val transaction = operation?.let { Transaction(it.id) }

                // The payments stream includes both sent and received payments.
                if (operation is PaymentOperationResponse) {
                    transaction?.apply {
                        sourceAddress = operation.from
                        destinationAddress = operation.to
                        amount = operation.amount
                        memo = server.transactions().transaction(operation.transactionHash).memo.toString()
                        date = operation.createdAt
                    }

                    if (operation.from == userId?.getAccountId())
                        transaction?.type = TransactionAdapter.DEBIT
                    else
                        transaction?.type = TransactionAdapter.CREDIT

                    transaction?.let {
                        this@TransactionsViewModel.viewModelScope.launch {
                            adapter.addTransaction(it)
                            recyclerView.scrollToPosition(0)
                            db.transactionsDao().insert(it)
                        }
                    }
                }
            }

            override fun onFailure(p0: Optional<Throwable>?, p1: Optional<Int>?) {
                TODO("throw exception")
            }
        })
    }
}