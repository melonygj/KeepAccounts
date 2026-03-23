package com.example.keepaccounts.ui.screens

import androidx.compose.foundation.layout.*
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
fun ProfileScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    var darkMode by remember { mutableStateOf(false) }
    
    Scaffold(topBar = { TopAppBar(title = { Text("我的", fontWeight = FontWeight.Bold) }) }) { padding ->
        Column(modifier.padding(padding)) {
            Card(Modifier.fillMaxWidth().padding(16.dp)) {
                Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountCircle, null, Modifier.size(64.dp), MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(16.dp))
                    Column { Text("记账用户", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("坚持记账，掌控财务", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                }
            }
            Text("设置", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(16.dp, 8.dp))
            Row(Modifier.fillMaxWidth().padding(16.dp, 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DarkMode, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) { Text("深色模式", style = MaterialTheme.typography.bodyLarge)
                    Text("保护眼睛", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                Switch(checked = darkMode, onCheckedChange = { darkMode = it })
            }
            Row(Modifier.fillMaxWidth().padding(16.dp, 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(16.dp))
                Column { Text("关于", style = MaterialTheme.typography.bodyLarge)
                    Text("版本 1.0.0", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
        }
    }
}