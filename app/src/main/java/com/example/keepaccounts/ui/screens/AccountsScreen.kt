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
import com.example.keepaccounts.data.model.Account
import com.example.keepaccounts.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val accounts by viewModel.accounts.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var newAccountName by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("账户管理", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "添加账户")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(accounts) { account ->
                AccountCard(account)
            }
        }
    }
    
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("添加账户") },
            text = {
                OutlinedTextField(
                    value = newAccountName,
                    onValueChange = { newAccountName = it },
                    label = { Text("账户名称") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newAccountName.isNotBlank()) {
                            viewModel.addAccount(Account(name = newAccountName, icon = "💰"))
                            newAccountName = ""
                        }
                        showAddDialog = false
                    }
                ) {
                    Text("添加")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun AccountCard(account: Account) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = account.icon, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.width(12.dp))
                Text(account.name, style = MaterialTheme.typography.titleMedium)
            }
            Text(
                "¥%.2f".format(account.balance),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}