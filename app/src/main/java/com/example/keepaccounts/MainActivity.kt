package com.example.keepaccounts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.keepaccounts.data.database.AppDatabase
import com.example.keepaccounts.ui.screens.*
import com.example.keepaccounts.ui.theme.KeepAccountsTheme
import com.example.keepaccounts.viewmodel.MainViewModel
import com.example.keepaccounts.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(applicationContext)
        
        setContent {
            KeepAccountsTheme {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(database)
                )
                
                // 初始化默认数据
                LaunchedEffect(Unit) {
                    viewModel.initializeData()
                }
                
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("首页") }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
                    label = { Text("账户") }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Savings, contentDescription = null) },
                    label = { Text("借贷") }
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("我的") }
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> HomeScreen(viewModel, Modifier.padding(paddingValues))
            1 -> AccountsScreen(viewModel, Modifier.padding(paddingValues))
            2 -> DebtScreen(viewModel, Modifier.padding(paddingValues))
            3 -> ProfileScreen(viewModel, Modifier.padding(paddingValues))
        }
    }
}