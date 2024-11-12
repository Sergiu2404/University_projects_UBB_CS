package com.example.expense_tracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.expense_tracker.domain.Expense

class ExpenseDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val expense = intent.getParcelableExtra<Expense>("viewExpense")

        setContent(){
            if(expense == null) ExpenseDetail(Expense(0, 0.00, "", "", "", ""))
            else
                ExpenseDetail(expense)
        }

    }
}


@Composable
fun ExpenseDetail(expense: Expense) {
    var amount by remember { mutableStateOf(expense.amount.toString()) }
    var category by remember { mutableStateOf(expense.category ?: "") }
    var date by remember { mutableStateOf(expense.date ?: "") }
    var type by remember { mutableStateOf(expense.type ?: "") }
    var notes by remember { mutableStateOf(expense.notes ?: "") }
    var errorMessage by remember { mutableStateOf("") }

    // Validation for amount input
    fun isValidAmount(input: String): Boolean {
        return input.isNotEmpty() && input.toDoubleOrNull() != null
    }

    Scaffold(
        topBar = { ViewExpenseToolbar() },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text("View / Edit Expense", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                BasicTextFieldWithLabel(
                    label = "Amount",
                    value = amount,
                    onValueChange = {
                        // Allow only digits and decimal point
                        if (it.isEmpty() || it.all { char -> char.isDigit() || char == '.' }) {
                            amount = it
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Decimal
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextFieldWithLabel(
                    label = "Category",
                    value = category,
                    onValueChange = { category = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextFieldWithLabel(
                    label = "Date (yyyy-mm-dd)",
                    value = date,
                    onValueChange = { date = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Type (SPENDING / RECEIVING)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == "SPENDING",
                            onClick = { type = "SPENDING" }
                        )
                        Text("SPENDING", modifier = Modifier.padding(start = 8.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == "RECEIVING",
                            onClick = { type = "RECEIVING" }
                        )
                        Text("RECEIVING", modifier = Modifier.padding(start = 8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                BasicTextFieldWithLabel(
                    label = "Notes",
                    value = notes,
                    onValueChange = { notes = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Show error message if validation fails
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val context = LocalContext.current

                Button(
                    onClick = {
                        if (amount.isEmpty() || !isValidAmount(amount)) {
                            errorMessage = "Please enter a valid amount."
                        } else if (category.isEmpty()) {
                            errorMessage = "Category cannot be empty."
                        } else if (date.isEmpty()) {
                            errorMessage = "Date cannot be empty."
                        } else if (type.isEmpty()) {
                            errorMessage = "Please select the type (SPENDING/RECEIVING)."
                        } else {
                            errorMessage = "" // Clear error message

                            val updatedExpense = Expense(
                                expense.id,
                                amount.toDouble(),
                                category,
                                date,
                                type,
                                notes
                            )

                            // Pass the updated expense back to the previous activity
                            val resultIntent = Intent().apply {
                                putExtra("updatedExpense", updatedExpense)
                            }

                            // Set the result and finish
                            (context as? Activity)?.setResult(Activity.RESULT_OK, resultIntent)
                            (context as? Activity)?.finish()
                        }
                    }
                ) {
                    Text("Save")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewExpenseToolbar() {
    TopAppBar(
        title = {
            Text(
                text = "View / Edit Expense",
                fontSize = 17.sp,
                color = Color.Black
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF32A146)
        )
    )
}

