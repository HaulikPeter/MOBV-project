package com.example.stellar.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.stellar.databinding.FragmentNewTransactionBinding
import com.example.stellar.ui.login.LoginRepository
import org.stellar.sdk.*

class NewTransactionFragment : Fragment() {

    private lateinit var binding: FragmentNewTransactionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentNewTransactionBinding.inflate(inflater, container, false)

        val btnCommit = binding.btnCommit
        val etAddress = binding.etAddress
        val etAmount = binding.etAmount
        val etMemo = binding.etMemo

        btnCommit.setOnClickListener {
            val server = Server("https://horizon-testnet.stellar.org")
            val user = LoginRepository.getInstance().user
            val destination = KeyPair.fromAccountId(etAddress.text.toString())
            Thread {
                try {
                    val account = server.accounts().account(user?.getAccountId())
                    val transaction = Transaction.Builder(account, Network.TESTNET)
                        .addOperation(
                            PaymentOperation.Builder(
                                destination.accountId,
                                AssetTypeNative(),
                                etAmount.text.toString()
                            ).build()
                        )
                        .addMemo(Memo.text(etMemo.text.toString()))
                        .setTimeout(180)
                        .setBaseFee(Transaction.MIN_BASE_FEE)
                        .build()

                    transaction.sign(user?.getKeyPair())

                    try {
                        server.submitTransaction(transaction)
                        activity?.finish()
                    } catch (e: Throwable) {
                        println("Submit to server FAILED")
                        e.printStackTrace()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }.start()
        }
        return binding.root
    }

}