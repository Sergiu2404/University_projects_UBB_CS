package com.example.ezpensetracker

import Expense
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ezpensetracker.databinding.ActivityExpenseDetailBinding

class ExpenseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseDetailBinding
    private lateinit var expense: Expense

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get the exp for which user clicked view
        expense = intent.getParcelableExtra<Expense>("EXTRA_EXPENSE") ?: return

        // populate with exp details
        populateExpenseDetails()

        binding.submitButton.setOnClickListener {
            updateExpense()
        }
    }

    private fun populateExpenseDetails() {
        binding.idValue.text = "#${expense.id}"

        // fill input fields with exp details received from main
        binding.amountInput.setText(expense.amount.toString())
        binding.typeInput.setText(expense.type)
        binding.categoryInput.setText(expense.category)
        binding.dateInput.setText(expense.date)
        binding.noteInput.setText(expense.notes)
    }

    private fun updateExpense() {
        // Get the values from the inputs
        val updatedAmount = binding.amountInput.text.toString().toDoubleOrNull()
        val updatedType = binding.typeInput.text.toString()
        val updatedCategory = binding.categoryInput.text.toString()
        val updatedDate = binding.dateInput.text.toString()
        val updatedNotes = binding.noteInput.text.toString()

        if (updatedAmount == null || updatedAmount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        if (updatedType.isBlank()) {
            Toast.makeText(this, "Type must not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (updatedCategory.isBlank()) {
            Toast.makeText(this, "Category cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (updatedDate.isBlank()) {
            Toast.makeText(this, "Date cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if (updatedNotes.length > 300) {
            Toast.makeText(this, "Notes cannot exceed 300 characters", Toast.LENGTH_SHORT).show()
            return
        }

        expense.amount = updatedAmount
        expense.type = updatedType
        expense.category = updatedCategory
        expense.date = updatedDate
        expense.notes = updatedNotes

        // Send updated expense back to main
        val resultIntent = intent.apply {
            putExtra("EXTRA_UPDATED_EXPENSE", expense)
        }
        setResult(Activity.RESULT_OK, resultIntent)

        // Show confirmation message
        Toast.makeText(this, "Expense updated successfully!", Toast.LENGTH_SHORT).show()

        // Pop activity and go to the previous screen
        finish()
    }
}
