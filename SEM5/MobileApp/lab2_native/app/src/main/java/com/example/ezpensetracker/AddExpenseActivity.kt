package com.example.ezpensetracker

import Expense
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ezpensetracker.databinding.ActivityAddExpenseBinding

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the toolbar
//        setSupportActionBar(binding.myToolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // add exp
        binding.submitButton.setOnClickListener {
            try {
                val amount = binding.amountInput.text.toString().toDoubleOrNull()
                val type = binding.typeInput.text.toString()
                val category = binding.categoryInput.text.toString()
                val date = binding.dateInput.text.toString()
                val notes = binding.noteInput.text.toString()

                if (amount != null && type.isNotEmpty() && category.isNotEmpty() && date.isNotEmpty()) {
                    val newExpense = Expense(
                        id = 0,  // handled in main
                        amount = amount,
                        type = type,
                        category = category,
                        date = date,
                        notes = notes
                    )

                    //send back to main
                    val resultIntent = Intent().apply {
                        putExtra("EXTRA_NEW_EXPENSE", newExpense)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish() //close add window
                } else {
                    Log.e("AddExpenseActivity", "One or more fields are invalid.")
                    if (amount == null) binding.amountInput.error = "Invalid amount"
                    if (type.isEmpty()) binding.typeInput.error = "Type is required"
                    if (category.isEmpty()) binding.categoryInput.error = "Category is required"
                    if (date.isEmpty()) binding.dateInput.error = "Date is required"
                }
            } catch (e: Exception) {
                Log.e("AddExpenseActivity", "Error while adding expense: ${e.message}")
            }
        }
    }
}
