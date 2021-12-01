package com.example.stellar.ui.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.stellar.data.database.StellarDatabase
import com.example.stellar.data.database.StellarDatabaseRepository
import com.example.stellar.databinding.FragmentNewTransactionBinding
import com.example.stellar.ui.auth.PromptPinDialogFragment
import com.example.stellar.ui.auth.addKey
import com.example.stellar.ui.auth.decrypt
import com.example.stellar.ui.login.LoginRepository
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.stellar.sdk.*

/*
Fragment for sending currency in form of transaction
If fields amount and public key are not fulfilled, the transaction cannot be commited
If fields amount and public key have wrong formula or the public key does not exist on stellar, an error is returned
Before commiting, pin code is required
 */

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

        val publicKey = requireActivity().intent.extras?.getString("publicKey")
        if (publicKey != null) {
            etAddress.setText(publicKey)
        }

        btnCommit.setOnClickListener {
            if (etAddress.text.isEmpty() || etAmount.text.isEmpty())
                return@setOnClickListener


            val repo = StellarDatabaseRepository(StellarDatabase.db(requireContext()).usersDao())
            lifecycleScope.launch {

                val user = repo.getUsers()

                val fragment = PromptPinDialogFragment { pin ->

                    try {
                        val pk = user?.get(0)?.privateKey?.decrypt(pin.addKey())?.toCharArray()
                        pk?.let { startConnection(it) }


                    } catch (e: Exception) {
                        Snackbar.make(
                            binding.root, "Wrong pin! Please try again!",
                            BaseTransientBottomBar.LENGTH_SHORT

                        ).show()
                    }
                }

                fragment.show(parentFragmentManager, "PromptPinDialogFragment")

            }

        }
        return binding.root
    }



    private fun startConnection(pk: CharArray) {
        val etAddress = binding.etAddress
        val etAmount = binding.etAmount
        val etMemo = binding.etMemo
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


                transaction.sign(KeyPair.fromSecretSeed(pk))

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

}