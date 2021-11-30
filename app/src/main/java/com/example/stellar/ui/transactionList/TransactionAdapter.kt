package com.example.stellar.ui.transactionList

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.stellar.R
import com.example.stellar.data.database.entities.Transaction

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    companion object {
        const val CREDIT = 0
        const val DEBIT = 1
    }

    private var transactionList = mutableListOf<Transaction>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactionList[position]
        holder.transaction = transaction

        if (transactionList[position].type == CREDIT)
            holder.cvListItemTransaction.setCardBackgroundColor(Color.GREEN)
        else
            holder.cvListItemTransaction.setCardBackgroundColor(Color.RED)

        holder.tvSourceAddress.text = transaction.sourceAddress
        holder.tvDestinationAddress.text = transaction.destinationAddress
        holder.tvTransactionAmount.text = transaction.amount
        holder.tvTransactionMemo.text = transaction.memo
        holder.tvTransactionDate.text = transaction.date

    }

    fun addTransaction(transaction: Transaction) {
        transactionList.add(0, transaction)
        notifyItemInserted(0)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addAllTransactions(transactions: MutableList<Transaction>) {
        transactionList = transactions
        notifyDataSetChanged()
    }

    fun contains(id: Long) = transactionList.find { t -> t.uid == id } != null

    override fun getItemCount() = transactionList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cvListItemTransaction: CardView = itemView.findViewById(R.id.cvListItemTransaction)
        var transaction: Transaction? = null
        val tvSourceAddress: TextView = itemView.findViewById(R.id.tvSourceAddress)
        val tvTransactionAmount: TextView = itemView.findViewById(R.id.tvTransactionAmount)
        val tvDestinationAddress: TextView = itemView.findViewById(R.id.tvDestinationAddress)
        val tvTransactionMemo: TextView = itemView.findViewById(R.id.tvTransactionMemo)
        val tvTransactionDate: TextView = itemView.findViewById(R.id.tvTransactionDate)
    }

}