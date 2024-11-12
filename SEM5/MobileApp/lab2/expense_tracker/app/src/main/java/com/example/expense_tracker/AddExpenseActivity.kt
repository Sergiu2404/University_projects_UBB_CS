package com.example.expense_tracker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.expense_tracker.domain.Expense
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddExpenseScreen() {
                finish()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(onFinish: () -> Unit) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDate = dateFormat.format(Date())

    // Dropdown menu for type (SPENDING or RECEIVING)
    val types = listOf("SPENDING", "RECEIVING")

    fun isValidAmount(amount: String): Boolean {
        return amount.toDoubleOrNull() != null
    }

    Scaffold(
        topBar = { AddExpenseToolbar() },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text("Add Expense", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                // Amount Field
                BasicTextFieldWithLabel(
                    label = "Amount",
                    value = amount,
                    onValueChange = { amount = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category Field
                BasicTextFieldWithLabel(
                    label = "Category",
                    value = category,
                    onValueChange = { category = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Date Field
                BasicTextFieldWithLabel(
                    label = "Date (yyyy-mm-dd)",
                    value = date.takeIf { it.isNotEmpty() } ?: currentDate,
                    onValueChange = { date = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Type Field (SPENDING / RECEIVING)


                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // SPENDING Radio Button
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == "SPENDING",
                            onClick = { type = "SPENDING" }
                        )
                        Text("SPENDING", modifier = Modifier.padding(start = 8.dp))
                    }

                    // RECEIVING Radio Button
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


                // Notes Field
                BasicTextFieldWithLabel(
                    label = "Notes",
                    value = notes,
                    onValueChange = { notes = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Error message if validation fails
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
                        if (amount.isEmpty() || category.isEmpty() || type.isEmpty() || notes.isEmpty()) {
                            errorMessage = "All fields are required."
                        } else if (!isValidAmount(amount)) {
                            errorMessage = "Amount must be a valid number."
                        } else if (type.isEmpty() || type !in types) {
                            errorMessage = "Type must be either SPENDING or RECEIVING."
                        } else {
                            val expense = Expense(
                                id = 0,
                                amount = amount.toDouble(),
                                category = category,
                                date = currentDate,  // Use current date or a date picker
                                type = type,
                                notes = notes
                            )

                            errorMessage = ""

                            val intent = Intent(context, MainActivity::class.java)
                            intent.putExtra("expense", expense)  // Sending the expense as an extra
                            context.startActivity(intent)
                            (context as Activity).finish()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32A146))
                ) {
                    Text("Add Expense", color = Color.White)
                }
            }
        }
    )
}


@Composable
fun BasicTextFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(4.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .padding(horizontal = 8.dp, vertical = 16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseToolbar() {
    TopAppBar(
        title = { Text("Add New Expense") },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF32A146)),
        navigationIcon = {
            IconButton(onClick = { /* Handle back action */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
        }
    )
}
