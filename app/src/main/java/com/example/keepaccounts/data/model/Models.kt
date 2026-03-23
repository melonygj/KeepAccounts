package com.example.keepaccounts.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// 收支类型
enum class TransactionType {
    INCOME, EXPENSE
}

// 分类
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val icon: String,
    val color: String
)

// 账户
@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val balance: Double = 0.0,
    val icon: String
)

// 交易记录
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: TransactionType,
    val amount: Double,
    val categoryName: String,
    val accountName: String,
    val date: Date,
    val note: String? = null
)