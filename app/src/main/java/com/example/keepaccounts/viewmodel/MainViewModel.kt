package com.example.keepaccounts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.keepaccounts.data.database.AppDatabase
import com.example.keepaccounts.data.model.*
import com.example.keepaccounts.data.repository.AccountRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(private val repository: AccountRepository) : ViewModel() {
    
    private val _selectedYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    val selectedYear: StateFlow<Int> = _selectedYear
    
    private val _selectedMonth = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH) + 1)
    val selectedMonth: StateFlow<Int> = _selectedMonth
    
    val accounts: StateFlow<List<Account>> = repository.getAllAccounts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val transactions: StateFlow<List<Transaction>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val expenseCategories: StateFlow<List<Category>> = repository.getCategoriesByType(TransactionType.EXPENSE)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val incomeCategories: StateFlow<List<Category>> = repository.getCategoriesByType(TransactionType.INCOME)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val monthlyIncome: StateFlow<Double> = _selectedMonth.flatMapLatest { month ->
        _selectedYear.flatMapLatest { year ->
            repository.getMonthlyIncome(year, month).map { it ?: 0.0 }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
    
    val monthlyExpense: StateFlow<Double> = _selectedMonth.flatMapLatest { month ->
        _selectedYear.flatMapLatest { year ->
            repository.getMonthlyExpense(year, month).map { it ?: 0.0 }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
    
    val totalAssets: StateFlow<Double> = accounts.map { list ->
        list.sumOf { it.balance }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
    
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.addTransaction(transaction)
        }
    }
    
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }
    
    fun addAccount(account: Account) {
        viewModelScope.launch {
            repository.addAccount(account)
        }
    }
    
    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.addCategory(category)
        }
    }
    
    fun initializeData() {
        viewModelScope.launch {
            repository.initializeDefaultData()
        }
    }
}

class MainViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(AccountRepository(database)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}