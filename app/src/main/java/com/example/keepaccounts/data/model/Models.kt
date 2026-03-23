package com.example.keepaccounts.data.model

data class Account(
    val id: Long = 0,
    val name: String,
    val balance: Double = 0.0,
    val icon: String
)

data class Transaction(
    val id: Long = 0,
    val type: String, // "income" or "expense"
    val amount: Double,
    val category: String,
    val account: String,
    val note: String = "",
    val date: Long = System.currentTimeMillis()
)

data class Category(
    val id: Long = 0,
    val name: String,
    val type: String,
    val icon: String
)