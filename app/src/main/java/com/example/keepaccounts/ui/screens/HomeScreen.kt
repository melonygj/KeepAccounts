package com.example.keepaccounts.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.keepaccounts.data.model.*
import com.example.keepaccounts.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val accounts by viewModel.accounts.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val totalAssets by viewModel.totalAssets.collectAsState()
    val monthlyIncome by viewModel.monthlyIncome.collectAsState()
    val monthlyExpense by viewModel.monthlyExpense.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("记账本", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Search, contentDescription = "搜索")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("总资产", color = MaterialTheme.colorScheme.onPrimary)
                        Text(
                            "¥%.2f".format(totalAssets),
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("本月收入", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
                                Text("¥%.2f".format(monthlyIncome), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("本月支出", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
                                Text("¥%.2f".format(monthlyExpense), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickAction(icon = Icons.Default.Add, label = "记一笔") { }
                    QuickAction(icon = Icons.Default.AccountBalanceWallet, label = "账户") { }
                    QuickAction(icon = Icons.Default.PieChart, label = "报表") { }
                }
            }
            
            item {
                Text(
                    "最近交易",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
            
            items(transactions.take(10)) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun QuickAction(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        FilledIconButton(onClick = onClick) {
            Icon(icon, contentDescription = label)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(transaction.categoryName, style = MaterialTheme.typography.titleMedium)
                Text(
                    transaction.note ?: transaction.accountName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    SimpleDateFormat("MM-dd HH:mm").format(transaction.date),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = if (transaction.type == TransactionType.INCOME) 
                    "+%.2f".format(transaction.amount) 
                else 
                    "-%.2f".format(transaction.amount),
                style = MaterialTheme.typography.titleMedium,
                color = if (transaction.type == TransactionType.INCOME) 
                    com.example.keepaccounts.ui.theme.IncomeColor 
                else 
                    com.example.keepaccounts.ui.theme.ExpenseColor
            )
        }
    }
}