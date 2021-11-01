package com.example.stellar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.stellar.databinding.FragmentHomeBinding
import org.json.JSONException

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val qrCode = binding.qrCode
        val tvAddress = binding.tvAddress
        val tvBalance = binding.tvBalance

        homeViewModel.getUser()?.observe(viewLifecycleOwner, { user ->
            val queue = Volley.newRequestQueue(context)
            val url = "https://horizon-testnet.stellar.org/accounts/" + user?.getAccountId()
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                try {
                    println("Success")
                    val accountId = response.getString("account_id")
                    val balance =
                        response.getJSONArray("balances").getJSONObject(0).getString("balance")
                    tvAddress.text = accountId
                    tvBalance.text = balance
                    qrCode.loadUrl("https://chart.googleapis.com/chart?chs=170x170&chld=M%7C0&cht=qr&chl=" + user?.getAccountId())
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
        })
        return binding.root
    }
}