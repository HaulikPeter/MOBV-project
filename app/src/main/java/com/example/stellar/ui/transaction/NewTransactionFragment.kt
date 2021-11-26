package com.example.stellar.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.stellar.databinding.FragmentNewTransactionBinding
import com.example.stellar.ui.login.LoginRepository
import kotlinx.coroutines.launch
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

        val publicKey = requireActivity().intent.extras?.getString("publicKey")
        if (publicKey != null) {
            etAddress.setText(publicKey)
        }

        btnCommit.setOnClickListener {
            if (etAddress.text.isEmpty() || etAmount.text.isEmpty())
                return@setOnClickListener

            Thread {
                try {
                    val server = Server("https://horizon-testnet.stellar.org")

                    server.accounts().account(etAddress.text.toString())

                    val user = LoginRepository.getInstance().user
                    val destination = KeyPair.fromAccountId(etAddress.text.toString())

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
                    lifecycleScope.launch {
                        Toast.makeText(
                            this@NewTransactionFragment.requireContext(),
                            "Incorrect credentials!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.start()
        }
        return binding.root
    }

}