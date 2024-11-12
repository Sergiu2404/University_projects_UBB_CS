package com.example.ezpensetracker

import Expense
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ezpensetracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mainActivityBinding: ActivityMainBinding
    private lateinit var expenseAdapter: ExpenseAdapter
    private val expenseService = ExpenseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivityBinding.root)

        setupRecyclerView()

        //register activity result launcher for updating an existing expense
        val updateExpenseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updatedExpense = result.data?.getParcelableExtra<Expense>("EXTRA_UPDATED_EXPENSE")
                updatedExpense?.let { //if exp got from parcel not null then update
                    //updateethe list and notify the adapter
                    updateExpense(it)
                }
            }
        }

        // register launcher for add expense
        val addExpenseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val newExpense = result.data?.getParcelableExtra<Expense>("EXTRA_NEW_EXPENSE")
                newExpense?.let { // if added exp not null then add it to the adapter list
                    addExpense(it)
                }
            }
        }

        //send to add expense
        mainActivityBinding.addExpenseButton.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            addExpenseLauncher.launch(intent)
        }

        // handle click on every single exp
        expenseAdapter.setOnExpenseClickListener { expense ->
            // open ExpenseDetailActivity to view or update the specific exp
            val intent = Intent(this, ExpenseDetailActivity::class.java).apply {
                putExtra("EXTRA_EXPENSE", expense)
            }
            updateExpenseLauncher.launch(intent)
        }
    }

    private fun setupRecyclerView() {
        val expenses = expenseService.getExpenses()
        // convert to muttable list for dynamic updates
        val mutableExpenses = expenses.toMutableList()
        // Initialize adapter with the mutable list
        expenseAdapter = ExpenseAdapter(mutableExpenses)
        // set recycler s vies layout manager for vertical linear list and also the adapter
        mainActivityBinding.expensesRecyclerView.layoutManager = LinearLayoutManager(this)
        mainActivityBinding.expensesRecyclerView.adapter = expenseAdapter
    }

    private fun updateExpense(updatedExpense: Expense) {
        // pos of updated exp in list
        val position = expenseAdapter.expenseList.indexOfFirst { it.id == updatedExpense.id }
        if (position != -1) {
            // update exp in adapter's list
            expenseAdapter.expenseList[position] = updatedExpense
            // notify and refresh the adapter
            expenseAdapter.notifyItemChanged(position)
        }
    }

    private fun addExpense(newExpense: Expense) {
        // add exp and notify the adapter
        expenseAdapter.expenseList.add(newExpense)
        expenseAdapter.notifyItemInserted(expenseAdapter.expenseList.size - 1)
    }
}
