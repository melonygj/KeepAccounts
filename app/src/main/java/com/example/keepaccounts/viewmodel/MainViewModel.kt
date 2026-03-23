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
    
    // UI 状态
    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab
    
    private val _selectedYear = MutableStateFlow(Calendar.getInstance().get(Calendar.YEAR))
    val selectedYear: StateFlow<Int> = _selectedYear
    
    private val _selectedMonth = MutableStateFlow(Calendar.getInstance().get(Calendar.MONTH) + 1)
    val selectedMonth: StateFlow<Int> = _selectedMonth
    
    // 数据流
    val accounts: StateFlow<List<Account>> = repository.getAllAccounts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val transactions: StateFlow<List<Transaction>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val expenseCategories: StateFlow<List<Category>> = repository.getCategoriesByType(TransactionType.EXPENSE)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val incomeCategories: StateFlow<List<Category>> = repository.getCategoriesByType(TransactionType.INCOME)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val budgets: StateFlow<List<Budget>> = repository.getAllBudgets()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    val debts: StateFlow<List<Debt>> = repository.getActiveDebts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    
    // 统计数据
    val monthlyIncome: StateFlow<Double> = combine(_selectedYear, _selectedMonth) { year, month ->
        repository.getMonthlyIncome(year, month).first() ?: 0.0
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
    
    val monthlyExpense: StateFlow<Double> = combine(_selectedYear, _selectedMonth) { year, month ->
        repository.getMonthlyExpense(year, month).first() ?: 0.0
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
    
    // 总资产
    val totalAssets: StateFlow<Double> = accounts.map { list ->
        list.filter { it.includeInTotal }.sumOf { it.balance }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)
    
    // 操作方法
    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }
    
    fun setSelectedYear(year: Int) {
        _selectedYear.value = year
    }
    
    fun setSelectedMonth(month: Int) {
        _selectedMonth.value = month
    }
    
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
    
    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            repository.deleteAccount(account)
        }
    }
    
    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.addCategory(category)
        }
    }
    
    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            repository.addBudget(budget)
        }
    }
    
    fun addDebt(debt: Debt) {
        viewModelScope.launch {
            repository.addDebt(debt)
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