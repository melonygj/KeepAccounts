package com.example.keepaccounts.data.repository

import com.example.keepaccounts.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AccountRepository {
    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts
    
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions
    
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories
    
    init {
        initializeDefaultData()
    }
    
    fun addAccount(account: Account) {
        _accounts.value = _accounts.value + account.copy(id = (_accounts.value.maxOfOrNull { it.id } ?: 0) + 1)
    }
    
    fun addTransaction(transaction: Transaction) {
        _transactions.value = _transactions.value + transaction.copy(id = (_transactions.maxOfOrNull { it.id } ?: 0) + 1)
    }
    
    fun getMonthlyTotal(type: String): Double {
        val now = System.currentTimeMillis()
        val monthStart = now - (now % (30L * 24 * 60 * 60 * 1000))
        return _transactions.value
            .filter { it.type == type && it.date >= monthStart }
            .sumOf { it.amount }
    }
    
    private fun initializeDefaultData() {
        _accounts.value = listOf(
            Account(1, "现金", 1000.0, "💵"),
            Account(2, "支付宝", 5000.0, "📱"),
            Account(3, "微信", 3000.0, "💚")
        )
        
        _categories.value = listOf(
            Category(1, "餐饮", "expense", "🍽️"),
            Category(2, "交通", "expense", "🚗"),
            Category(3, "购物", "expense", "🛒"),
            Category(4, "工资", "income", "💰"),
            Category(5, "奖金", "income", "🎁")
        )
        
        _transactions.value = listOf(
            Transaction(1, "expense", 35.0, "餐饮", "支付宝", "午餐", System.currentTimeMillis() - 3600000),
            Transaction(2, "expense", 15.0, "交通", "微信", "地铁", System.currentTimeMillis() - 7200000),
            Transaction(3, "income", 10000.0, "工资", "银行卡", "月薪", System.currentTimeMillis() - 86400000)
        )
    }
}