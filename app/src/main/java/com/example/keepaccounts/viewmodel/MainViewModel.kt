package com.example.keepaccounts.viewmodel

import androidx.lifecycle.ViewModel
import com.example.keepaccounts.data.model.*
import com.example.keepaccounts.data.repository.AccountRepository
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val repository = AccountRepository()
    
    val accounts: StateFlow<List<Account>> = repository.accounts
    val transactions: StateFlow<List<Transaction>> = repository.transactions
    val categories: StateFlow<List<Category>> = repository.categories
    
    val totalAssets: Double
        get() = accounts.value.sumOf { it.balance }
    
    val monthlyIncome: Double
        get() = repository.getMonthlyTotal("income")
    
    val monthlyExpense: Double
        get() = repository.getMonthlyTotal("expense")
    
    fun addAccount(name: String) {
        repository.addAccount(Account(name = name, icon = "💰"))
    }
    
    fun addTransaction(type: String, amount: Double, category: String, account: String, note: String) {
        repository.addTransaction(Transaction(type = type, amount = amount, category = category, account = account, note = note))
    }
}