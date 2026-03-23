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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val accounts by viewModel.accounts.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = { TopAppBar(title = { Text("账户管理", fontWeight = FontWeight.Bold) }) },
        floatingActionButton = { FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Default.Add, null) } }
    ) { padding ->
        LazyColumn(modifier.padding(padding)) {
            items(accounts) { a ->
                Card(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(a.icon, style = MaterialTheme.typography.headlineMedium)
                            Spacer(Modifier.width(12.dp))
                            Text(a.name, style = MaterialTheme.typography.titleMedium)
                        }
                        Text("¥%.2f".format(a.balance), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
    
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, title = { Text("添加账户") }, text = { OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("名称") }, modifier = Modifier.fillMaxWidth()) },
            confirmButton = { TextButton(onClick = { if(name.isNotBlank()) { viewModel.addAccount(name); name = "" }; showDialog = false }) { Text("添加") } },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("取消") } })
    }
}