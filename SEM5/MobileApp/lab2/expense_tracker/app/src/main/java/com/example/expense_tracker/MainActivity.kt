package com.example.expense_tracker


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense_tracker.domain.Expense
import com.example.expense_tracker.ui.theme.Expense_trackerTheme


class MainActivity : ComponentActivity() {
    // create new instance of a view model
    private val expenseViewModel: ExpenseViewModel by viewModels()

    private val updateExpenseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        println("does not enter here")
        if (result.resultCode == Activity.RESULT_OK) {
            println("does not arrive here")
            val updatedExpense = result.data?.getParcelableExtra<Expense>("updatedExpense")
            if (updatedExpense != null) {
                expenseViewModel.updateExpense(updatedExpense)
            }

        }
    }

    private val addExpenseLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        //println("does not enter here")
        if (result.resultCode == Activity.RESULT_OK) {
            //println("does not arrive here")
            val newExpense = result.data?.getParcelableExtra<Expense>("newExpense")
            if (newExpense != null) {
                expenseViewModel.addExpense(newExpense)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

//            val expense = intent.getParcelableExtra<Expense>("expense")
//            if(expense != null)
//                expenseViewModel.addExpense(expense)
            MainScreen(expenseViewModel)
        }
    }


    fun startViewExpenseActivity(expense: Expense) {
        val intent = Intent(this, ExpenseDetailActivity::class.java)
            .apply {
            putExtra("viewExpense", expense)
            println("extra viewExpense")
        }
        //startActivity(intent)
        updateExpenseLauncher.launch(intent)
    }

    fun startAddExpenseActivity() {
        val intent = Intent(this, AddExpenseActivity::class.java)
        //startActivity(intent)
        addExpenseLauncher.launch(intent)
    }

}



@Composable
fun MainScreen(expenseViewModel: ExpenseViewModel) {
    val expenses by expenseViewModel.expenses.observeAsState(emptyList()) // Observing LiveData

    Scaffold(
        topBar = { ExpenseTrackerToolbar() },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                ExpensesTitle()
                Spacer(modifier = Modifier.height(16.dp))
                AddExpenseButton(expenseViewModel)
                Spacer(modifier = Modifier.height(8.dp))
                expenses?.let { ExpenseList(it, expenseViewModel) } // List of expenses
            }
        }
    )
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseTrackerToolbar() {
    TopAppBar(
        title =
        {
            Text(
                text = "My Financial Tracker",
                fontSize = 17.sp,
                color = Color.Black
            )
        }
        ,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF32A146)
        )
    )
}

@Composable
fun ExpensesTitle() {
    Text(
        text = "Expenses List",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(align = Alignment.Center),
    )
}

@Composable
fun AddExpenseButton(expenseViewModel: ExpenseViewModel) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = "Add Expense",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .clickable {
                    if (context is MainActivity) {
                        context.startAddExpenseActivity()
                        //println("enetered startViewAct")
                    }
//                    val intent = Intent(context, AddExpenseActivity::class.java)
//                    context.startActivity(intent)
                }
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun ExpenseList(expenses: List<Expense>, expenseViewModel: ExpenseViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(expenses) { expense ->
            Spacer(modifier = Modifier.height(20.dp))
            ExpenseItem(expense, expenseViewModel)
        }
    }
}


@Composable
fun ExpenseItem(expense: Expense, expenseViewModel: ExpenseViewModel) {
    val openDialog = remember { mutableStateOf(false) } //remember stores states through recompositions
    val showConfirmDeleteDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFC1F7C7))
            .padding(16.dp)
            .padding(bottom = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${expense.amount} RON",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "ID: ${expense.id}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { openDialog.value = true },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "More", fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = expense.category,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )

            Text(
                text = expense.date,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Text(
                text = expense.type,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (expense.notes.isNotBlank()) {
            Text(
                text = expense.notes,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    val context = LocalContext.current

    if (openDialog.value) {
        ExpenseActionDialog(
            onViewClicked = {
                if (context is MainActivity) {
                    context.startViewExpenseActivity(expense)
                    openDialog.value = false
                    //println("enetered startViewAct")
                }
                println("view button clicked")
            },
            onDeleteClicked = {
                println("delete button clicked")
                openDialog.value = false
                showConfirmDeleteDialog.value = true
            },
            onDismiss = { openDialog.value = false }
        )
    }

    if (showConfirmDeleteDialog.value) {
        ConfirmationDeleteDialog(
            onConfirmDelete = {
                expenseViewModel.deleteExpenseById(expense.id)
                showConfirmDeleteDialog.value = false
            },
            onDismiss = {
                showConfirmDeleteDialog.value = false
            }
        )
    }
}



@Composable
fun ConfirmationDeleteDialog(
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Are you sure?") },
        text = { Text("This action will permanently delete the expense.") },
        confirmButton = {
            TextButton(onClick = onConfirmDelete) {
                Text("Delete", fontWeight = FontWeight.Bold, color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", fontWeight = FontWeight.Bold)
            }
        }
    )
}


@Composable
fun ExpenseActionDialog(
    onViewClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Choose action") },
        confirmButton = {
            TextButton(onClick = onViewClicked) {
                Text("View", fontWeight = FontWeight.Bold, color = Color.Blue)
            }
        },
        dismissButton = {
            TextButton(onClick = onDeleteClicked) {
                Text("Delete", fontWeight = FontWeight.Bold, color = Color.Red)
            }
        }
    )
}


