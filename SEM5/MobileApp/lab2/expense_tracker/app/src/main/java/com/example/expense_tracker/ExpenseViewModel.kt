package com.example.expense_tracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.expense_tracker.domain.Expense


class ExpenseViewModel : ViewModel() {

    // Internal mutable LiveData to store expenses
    private val _expenses = MutableLiveData<List<Expense>?>()

    // Public immutable LiveData to expose expenses list
    val expenses: LiveData<List<Expense>?> get() = _expenses

    private var lastId = 2  // Starting from 2 because we want the next expense to have id 3, assuming we have 2 hardcoded expenses

    private var hardcodedExpensesAdded = false

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        if (!hardcodedExpensesAdded) {
            // Initialize hardcoded expenses
            val hardcodedExpenses = listOf(
                Expense(id = 1, amount = 50.0, category = "Food", date = "2024-11-09", type = "SPENDING", notes = "Lunch"),
                Expense(id = 2, amount = 100.0, category = "Transport", date = "2024-11-09", type = "SPENDING", notes = "Bus fare")
            )


            _expenses.value = hardcodedExpenses
            hardcodedExpensesAdded = true
        }
    }

    fun addExpense(expense: Expense) {
        lastId++
        val newExpense = expense.copy(id = lastId)

        val currentList = _expenses.value.orEmpty().toMutableList()
        currentList.add(newExpense)

        // Update the LiveData with the new list
        _expenses.value = currentList
    }

    fun deleteExpenseById(expenseId: Int) {
        val currentList = _expenses.value.orEmpty().toMutableList()
        val updatedList = currentList.filter { it.id != expenseId }

        _expenses.value = updatedList
    }

    // Update an existing expense and refresh the list
    fun updateExpense(expense: Expense) {
        val updatedList = _expenses.value?.map {
            if (it.id == expense.id) expense else it
        }
        _expenses.value = updatedList
    }
}
