package com.example.stellar.ui.transactionList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stellar.databinding.FragmentTransactionsBinding

/*
Fragment for viewing all transactions in the Main activity
This fragment is responsible for creating a view for incoming transactions
Segments of binding are passed to the viewmodel
 */

class TransactionsFragment : Fragment() {

    private lateinit var transactionsViewModel: TransactionsViewModel
    private lateinit var binding: FragmentTransactionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionsBinding.inflate(inflater, container, false)
        transactionsViewModel =
            ViewModelProvider(this,
                TransactionsViewModelFactory(requireContext()))[TransactionsViewModel::class.java]

        binding.rvTransactionList.adapter = transactionsViewModel.adapter
        transactionsViewModel.loadDataFromServer(binding.rvTransactionList)

        return binding.root
    }

}