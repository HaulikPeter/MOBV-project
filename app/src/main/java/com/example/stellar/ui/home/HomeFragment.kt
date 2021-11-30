package com.example.stellar.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.stellar.R
import com.example.stellar.data.database.StellarDatabase
import com.example.stellar.data.database.StellarDatabaseRepository
import com.example.stellar.data.model.LoggedInUser
import com.example.stellar.databinding.FragmentHomeBinding
import com.example.stellar.ui.auth.PromptPinDialogFragment
import com.example.stellar.ui.auth.addKey
import com.example.stellar.ui.auth.decrypt
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.json.JSONException

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dbRepo: StellarDatabaseRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        binding.tvAddressPublic.setOnClickListener { copyTextViewContentToClipboard(it as TextView) }
        binding.tvAddressPrivate.setOnClickListener { showPrivateKeyOnClick() }
        binding.tvBalance.setOnClickListener { copyTextViewContentToClipboard(it as TextView) }

        dbRepo = StellarDatabaseRepository(StellarDatabase.db(this.requireContext()).usersDao())

        lifecycleScope.launch {
            val user = dbRepo.getUsers()?.get(0)
            if (user != null) {
                if (user.balance.isNullOrEmpty()) {
                    homeViewModel.getUser()?.observe(viewLifecycleOwner, { observeUser(it) })
                } else {
                    user.balance?.let { binding.tvBalance.text = it }
                }
            }
        }

        return binding.root
    }

    private fun copyTextViewContentToClipboard(textView: TextView) {
        val manager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Data", textView.text.toString())
        manager.setPrimaryClip(clipData)

        Toast.makeText(context, textView.hint.toString() + " copied!", Toast.LENGTH_SHORT).show()
    }

    private fun showPrivateKeyOnClick() {
        val repo = StellarDatabaseRepository(StellarDatabase.db(requireContext()).usersDao())
        lifecycleScope.launch {

            val user = repo.getUsers()

            val fragment = PromptPinDialogFragment { pin ->

                try {
                    binding.tvAddressPrivate.text = user?.get(0)?.privateKey?.decrypt(pin.addKey())
                    binding.tvAddressPrivate.setOnClickListener { copyTextViewContentToClipboard(it as TextView) }

                } catch (e: Exception) {
                    binding.tvAddressPrivate.text = resources.getText(R.string.require_pin)
                    Snackbar.make(
                        binding.root, "Wrong pin! Please try again!",
                        BaseTransientBottomBar.LENGTH_SHORT

                    ).show()
                }
            }
            fragment.show(parentFragmentManager, "PromptPinDialogFragment")

        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getUser()?.observe(viewLifecycleOwner, { observeUser(it) })
    }

    private fun observeUser(user: LoggedInUser?) {
        val qrCode = binding.qrCode
        val tvAddressPublic = binding.tvAddressPublic
        val tvBalance = binding.tvBalance
        val tvAddressPrivate = binding.tvAddressPrivate

        val queue = Volley.newRequestQueue(context)
        val url = "https://horizon-testnet.stellar.org/accounts/" + user?.getAccountId()
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                println("Success")
                val accountId = response.getString("account_id")
                val balance =
                    response.getJSONArray("balances").getJSONObject(0).getString("balance")
                tvAddressPublic.text = accountId
                tvBalance.text = balance

                //-------
                lifecycleScope.launch {
                    val userRepo = dbRepo.getUsers()?.get(0)
                    userRepo?.balance = balance
                    userRepo?.let { dbRepo.updateUser(it) }
                }
                //-------

                tvAddressPrivate.text = resources.getText(R.string.require_pin)
                qrCode.loadUrl("https://chart.googleapis.com/chart?chs=100x100&chld=M%7C0&cht=qr&chl=" + user?.getAccountId())
            } catch (e: JSONException) {
                println("PARSE Error")
                e.printStackTrace()
                println(e.message)
            }
        }) { error ->
            println("Error")
            println(error.message)
        }
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }
}