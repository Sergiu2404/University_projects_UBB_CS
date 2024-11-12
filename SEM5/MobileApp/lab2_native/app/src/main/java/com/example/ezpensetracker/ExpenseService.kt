package com.example.ezpensetracker

import Expense

class ExpenseService {
    private val expenses = mutableListOf(
        Expense(1, 2000.00, "Spending", "Food", "31/10/2024", "The food for the entire month. I passed the date as the end of the specific month"),
        Expense(2, 200.00, "Spending", "Gym", "14/10/2024", "The gym subscription"),
        Expense(3, 3795.00, "Receiving", "Salary", "10/10/2024", ""),
        Expense(4, 660.00, "Receiving", "Meal tickets", "05/10/2024", "Meal tickets for last month")
    )

    fun getExpenses(): List<Expense> {
        return expenses.toList()
    }

//    fun addExpense(expense: Expense) {
//        var maxId = 0
//
//        for (e in expenses) {
//            if (e.id > maxId) {
//                maxId = e.id
//            }
//        }
//
//        val nextId = maxId + 1
//
//        val expenseWithId = expense.copy(id = nextId)
//
//        expenses.add(expenseWithId)
//    }
//
//
//    fun updateExpense(updatedExpense: Expense): Boolean {
//        val index = expenses.indexOfFirst { it.id == updatedExpense.id }
//        return if (index != -1) {
//            expenses[index] = updatedExpense
//            true  // Successfully updated
//        } else {
//            false  // Expense not found
//        }
//    }
//
//    fun deleteExpense(expenseId: Int): Boolean {
//        val index = expenses.indexOfFirst { it.id == expenseId }
//        return if (index != -1) {
//            expenses.removeAt(index)
//            true  // Successfully deleted
//        } else {
//            false  // Expense not found
//        }
//    }
//
//    fun getExpenseById(id: Int): Expense? {
//        return expenses.find { it.id == id }
//    }
}