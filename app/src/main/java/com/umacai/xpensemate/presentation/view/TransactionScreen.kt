package com.umacai.xpensemate.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umacai.xpensemate.data.local.entity.TransactionEntity
import java.text.SimpleDateFormat
import java.util.*

import com.umacai.xpensemate.presentation.viewmodel.TransactionViewModel

@Composable
fun TransactionScreen(viewModel: TransactionViewModel = hiltViewModel()) {


    val transactions by viewModel.transactions.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Transaction List", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionEntity) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Description: ${transaction.description}", style = MaterialTheme.typography.bodyMedium)
            Text("Amount: ₹${transaction.amount}", style = MaterialTheme.typography.bodyLarge)
            Text("Date: ${dateFormat.format(transaction.date)}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTransactionScreen() {
    TransactionScreen()
}