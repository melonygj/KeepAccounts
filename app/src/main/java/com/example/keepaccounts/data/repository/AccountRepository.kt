package com.example.keepaccounts.data.repository

import com.example.keepaccounts.data.database.*
import com.example.keepaccounts.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class AccountRepository(private val database: AppDatabase) {
    private val accountDao = database.accountDao()
    private val transactionDao = database.transactionDao()
    private val categoryDao = database.categoryDao()
    
    fun getAllAccounts(): Flow<List<Account>> = accountDao.getAll()
    
    suspend fun addAccount(account: Account): Long = accountDao.insert(account)
    
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAll()
    
    fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val endTime = calendar.timeInMillis
        return transactionDao.getByDateRange(startTime, endTime)
    }
    
    suspend fun addTransaction(transaction: Transaction): Long = transactionDao.insert(transaction)
    
    suspend fun deleteTransaction(transaction: Transaction) = transactionDao.delete(transaction)
    
    fun getMonthlyIncome(year: Int, month: Int): Flow<Double?> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val endTime = calendar.timeInMillis
        return transactionDao.getTotalByType(TransactionType.INCOME, startTime, endTime)
    }
    
    fun getMonthlyExpense(year: Int, month: Int): Flow<Double?> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val endTime = calendar.timeInMillis
        return transactionDao.getTotalByType(TransactionType.EXPENSE, startTime, endTime)
    }
    
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>> = categoryDao.getByType(type)
    
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAll()
    
    suspend fun addCategory(category: Category): Long = categoryDao.insert(category)
    
    suspend fun deleteCategory(category: Category) = categoryDao.delete(category)
    
    suspend fun initializeDefaultData() {
        val defaultCategories = listOf(
            Category(name = "餐饮", type = TransactionType.EXPENSE, icon = "🍽️", color = "#FF6B6B"),
            Category(name = "交通", type = TransactionType.EXPENSE, icon = "🚗", color = "#4ECDC4"),
            Category(name = "购物", type = TransactionType.EXPENSE, icon = "🛒", color = "#45B7D1"),
            Category(name = "娱乐", type = TransactionType.EXPENSE, icon = "🎮", color = "#96CEB4"),
            Category(name = "其他支出", type = TransactionType.EXPENSE, icon = "📦", color = "#85C1E9"),
            Category(name = "工资", type = TransactionType.INCOME, icon = "💰", color = "#58D68D"),
            Category(name = "奖金", type = TransactionType.INCOME, icon = "🎁", color = "#F4D03F"),
            Category(name = "其他收入", type = TransactionType.INCOME, icon = "💵", color = "#85C1E9")
        )
        defaultCategories.forEach { categoryDao.insert(it) }
        
        val defaultAccounts = listOf(
            Account(name = "现金", icon = "💵"),
            Account(name = "支付宝", icon = "📱"),
            Account(name = "微信", icon = "💚")
        )
        defaultAccounts.forEach { accountDao.insert(it) }
    }
}