package com.example.ezpensetracker

import Expense
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.ezpensetracker.databinding.ItemExpenseBinding

class ExpenseAdapter(var expenseList: MutableList<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    private var onExpenseClickListener: ((Expense) -> Unit)? = null

    //when creating a new holder for new exp
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val bindingView = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(bindingView)
    }

    //bind each exp to a view (called when recycler view created or scrolled)
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]
        holder.bind(expense)
        holder.itemView.setOnClickListener {
            onExpenseClickListener?.invoke(expense)
        }
    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    // for allowing class ViewHolder to set the listener for the adapter
    fun setOnExpenseClickListener(listener: (Expense) -> Unit) {
        onExpenseClickListener = listener
    }

    inner class ExpenseViewHolder(private val bindingView: ItemExpenseBinding) : RecyclerView.ViewHolder(bindingView.root) {
        fun bind(expense: Expense) { //display every exp with on the specific view
            bindingView.expenseAmount.text = "${expense.amount} RON"
            bindingView.expenseCategory.text = expense.category
            bindingView.expenseDate.text = expense.date
            bindingView.expenseType.text = expense.type

            // setup for "more" button
            bindingView.expenseMenuButton.setOnClickListener {
                showOptionsDialog(expense)
            }
        }

        private fun showOptionsDialog(expense: Expense) {
            val options = arrayOf("View", "Delete")
            AlertDialog.Builder(bindingView.root.context)
                .setTitle("Select an action")
                .setItems(options) { dialog: DialogInterface, which: Int ->
                    when (which) {
                        0 -> onExpenseClickListener?.invoke(expense) // view click
                        1 -> deleteExpense(expense) // delete click
                    }
                }
                .show()
        }

        private fun deleteExpense(expense: Expense) {
            AlertDialog.Builder(bindingView.root.context)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Yes") { dialog, which ->
                    //remove the exp and its view
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        expenseList.removeAt(position)
                        notifyItemRemoved(position)
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }
    }
}
