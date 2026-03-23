package com.example.keepaccounts.data.repository

import com.example.keepaccounts.data.database.*
import com.example.keepaccounts.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import java.util.Date

class AccountRepository(private val database: AppDatabase) {
    private val accountDao = database.accountDao()
    private val transactionDao = database.transactionDao()
    private val categoryDao = database.categoryDao()
    private val budgetDao = database.budgetDao()
    private val debtDao = database.debtDao()
    
    // 账户操作
    fun getAllAccounts(): Flow<List<Account>> = accountDao.getAll()
    
    suspend fun addAccount(account: Account): Long = accountDao.insert(account)
    
    suspend fun updateAccount(account: Account) = accountDao.update(account)
    
    suspend fun deleteAccount(account: Account) = accountDao.delete(account)
    
    // 交易操作
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAll()
    
    fun getTransactionsByMonth(year: Int, month: Int): Flow<List<Transaction>> {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1, 0, 0, 0)
        val startTime = calendar.timeInMillis
        calendar.add(Calendar.MONTH, 1)
        val endTime = calendar.timeInMillis
        return transactionDao.getByDateRange(startTime, endTime)
    }
    
    suspend fun addTransaction(transaction: Transaction): Long {
        val id = transactionDao.insert(transaction)
        // 更新账户余额
        when (transaction.type) {
            TransactionType.INCOME -> accountDao.updateBalance(transaction.accountId, transaction.amount)
            TransactionType.EXPENSE -> accountDao.updateBalance(transaction.accountId, -transaction.amount)
            TransactionType.TRANSFER -> {
                accountDao.updateBalance(transaction.accountId, -transaction.amount)
                transaction.toAccountId?.let { accountDao.updateBalance(it, transaction.amount) }
            }
        }
        return id
    }
    
    suspend fun deleteTransaction(transaction: Transaction) {
        transactionDao.delete(transaction)
        // 恢复账户余额
        when (transaction.type) {
            TransactionType.INCOME -> accountDao.updateBalance(transaction.accountId, -transaction.amount)
            TransactionType.EXPENSE -> accountDao.updateBalance(transaction.accountId, transaction.amount)
            TransactionType.TRANSFER -> {
                accountDao.updateBalance(transaction.accountId, transaction.amount)
                transaction.toAccountId?.let { accountDao.updateBalance(it, -transaction.amount) }
            }
        }
    }
    
    // 统计
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
    
    // 分类操作
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>> = categoryDao.getByType(type)
    
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAll()
    
    suspend fun addCategory(category: Category): Long = categoryDao.insert(category)
    
    suspend fun deleteCategory(category: Category) = categoryDao.delete(category)
    
    // 预算操作
    fun getAllBudgets(): Flow<List<Budget>> = budgetDao.getAll()
    
    suspend fun addBudget(budget: Budget): Long = budgetDao.insert(budget)
    
    suspend fun deleteBudget(budget: Budget) = budgetDao.delete(budget)
    
    // 借贷操作
    fun getActiveDebts(): Flow<List<Debt>> = debtDao.getActive()
    
    fun getAllDebts(): Flow<List<Debt>> = debtDao.getAll()
    
    suspend fun addDebt(debt: Debt): Long = debtDao.insert(debt)
    
    suspend fun updateDebt(debt: Debt) = debtDao.update(debt)
    
    suspend fun deleteDebt(debt: Debt) = debtDao.delete(debt)
    
    // 初始化默认数据
    suspend fun initializeDefaultData() {
        // 默认分类
        val defaultCategories = listOf(
            // 支出分类
            Category(name = "餐饮", type = TransactionType.EXPENSE, icon = "🍽️", color = "#FF6B6B"),
            Category(name = "交通", type = TransactionType.EXPENSE, icon = "🚗", color = "#4ECDC4"),
            Category(name = "购物", type = TransactionType.EXPENSE, icon = "🛒", color = "#45B7D1"),
            Category(name = "娱乐", type = TransactionType.EXPENSE, icon = "🎮", color = "#96CEB4"),
            Category(name = "医疗", type = TransactionType.EXPENSE, icon = "🏥", color = "#FFEAA7"),
            Category(name = "教育", type = TransactionType.EXPENSE, icon = "📚", color = "#DDA0DD"),
            Category(name = "住房", type = TransactionType.EXPENSE, icon = "🏠", color = "#98D8C8"),
            Category(name = "通讯", type = TransactionType.EXPENSE, icon = "📱", color = "#F7DC6F"),
            Category(name = "其他", type = TransactionType.EXPENSE, icon = "📦", color = "#85C1E9"),
            // 收入分类
            Category(name = "工资", type = TransactionType.INCOME, icon = "💰", color = "#58D68D"),
            Category(name = "奖金", type = TransactionType.INCOME, icon = "🎁", color = "#F4D03F"),
            Category(name = "投资收益", type = TransactionType.INCOME, icon = "📈", color = "#5DADE2"),
            Category(name = "兼职", type = TransactionType.INCOME, icon = "💼", color = "#AF7AC5"),
            Category(name = "其他收入", type = TransactionType.INCOME, icon = "💵", color = "#85C1E9")
        )
        defaultCategories.forEach { categoryDao.insert(it) }
        
        // 默认账户
        val defaultAccounts = listOf(
            Account(name = "现金", type = AccountType.CASH, icon = "💵", color = "#4CAF50", isDefault = true),
            Account(name = "支付宝", type = AccountType.ALIPAY, icon = "📱", color = "#2196F3"),
            Account(name = "微信", type = AccountType.WECHAT, icon = "💚", color = "#8BC34A")
        )
        defaultAccounts.forEach { accountDao.insert(it) }
    }
}