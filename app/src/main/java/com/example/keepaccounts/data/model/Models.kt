package com.example.keepaccounts.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// 账户类型
enum class AccountType(val displayName: String, val icon: String) {
    CASH("现金", "💵"),
    BANK("银行卡", "🏦"),
    CREDIT("信用卡", "💳"),
    ALIPAY("支付宝", "📱"),
    WECHAT("微信", "💚"),
    INVESTMENT("投资", "📈"),
    OTHER("其他", "💰")
}

// 收支类型
enum class TransactionType {
    INCOME, EXPENSE, TRANSFER
}

// 分类
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val icon: String,
    val color: String,
    val isDefault: Boolean = false
)

// 账户
@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: AccountType,
    val balance: Double = 0.0,
    val icon: String,
    val color: String,
    val isDefault: Boolean = false,
    val includeInTotal: Boolean = true
)

// 交易记录
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val categoryId: Long,
    val accountId: Long,
    val toAccountId: Long? = null, // 转账目标账户
    val date: Date,
    val note: String? = null,
    val location: String? = null,
    val photoPath: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

// 预算
@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoryId: Long?, // null表示总预算
    val amount: Double,
    val period: BudgetPeriod,
    val startDate: Date,
    val alertThreshold: Double = 0.8 // 预警阈值
)

enum class BudgetPeriod {
    WEEKLY, MONTHLY, YEARLY
}

// 借贷记录
@Entity(tableName = "debts")
data class Debt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: DebtType,
    val person: String,
    val amount: Double,
    val remainingAmount: Double,
    val interestRate: Double? = null,
    val startDate: Date,
    val dueDate: Date? = null,
    val note: String? = null,
    val isSettled: Boolean = false
)

enum class DebtType {
    BORROW, LEND
}

// 还款记录
@Entity(tableName = "repayments")
data class Repayment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val debtId: Long,
    val amount: Double,
    val date: Date,
    val note: String? = null
)