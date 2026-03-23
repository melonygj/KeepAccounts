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
import com.example.keepaccounts.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val transactions by viewModel.transactions.collectAsState()
    
    Scaffold(topBar = { TopAppBar(title = { Text("记账本", fontWeight = FontWeight.Bold) }) }) { padding ->
        LazyColumn(modifier.padding(padding)) {
            item {
                Card(Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("总资产", color = MaterialTheme.colorScheme.onPrimary)
                        Text("¥%.2f".format(viewModel.totalAssets), style = MaterialTheme.typography.displayMedium, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("本月收入", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
                                Text("¥%.2f".format(viewModel.monthlyIncome), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("本月支出", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary)
                                Text("¥%.2f".format(viewModel.monthlyExpense), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            item { Text("最近交易", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp)) }
            items(transactions) { t ->
                Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column { Text(t.category, style = MaterialTheme.typography.titleMedium)
                            Text(t.note, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(SimpleDateFormat("MM-dd HH:mm").format(Date(t.date)), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                        Text(if(t.type == "income") "+%.2f".format(t.amount) else "-%.2f".format(t.amount), style = MaterialTheme.typography.titleMedium, color = if(t.type == "income") Color(0xFF4CAF50) else Color(0xFFF44336))
                    }
                }
            }
        }
    }
}

@Composable
fun Color(v: Long) = androidx.compose.ui.graphics.Color(v)