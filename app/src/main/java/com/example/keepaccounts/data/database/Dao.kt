package com.example.keepaccounts.data.database

import androidx.room.*
import com.example.keepaccounts.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories WHERE type = :type ORDER BY name")
    fun getByType(type: TransactionType): Flow<List<Category>>
    
    @Query("SELECT * FROM categories ORDER BY type, name")
    fun getAll(): Flow<List<Category>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category): Long
    
    @Update
    suspend fun update(category: Category)
    
    @Delete
    suspend fun delete(category: Category)
}

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY isDefault DESC, name")
    fun getAll(): Flow<List<Account>>
    
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getById(id: Long): Account?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account): Long
    
    @Update
    suspend fun update(account: Account)
    
    @Delete
    suspend fun delete(account: Account)
    
    @Query("UPDATE accounts SET balance = balance + :amount WHERE id = :id")
    suspend fun updateBalance(id: Long, amount: Double)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startTime AND :endTime ORDER BY date DESC")
    fun getByDateRange(startTime: Long, endTime: Long): Flow<List<Transaction>>
    
    @Insert
    suspend fun insert(transaction: Transaction): Long
    
    @Update
    suspend fun update(transaction: Transaction)
    
    @Delete
    suspend fun delete(transaction: Transaction)
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date BETWEEN :startTime AND :endTime")
    fun getTotalByType(type: TransactionType, startTime: Long, endTime: Long): Flow<Double?>
}

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets")
    fun getAll(): Flow<List<Budget>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget): Long
    
    @Update
    suspend fun update(budget: Budget)
    
    @Delete
    suspend fun delete(budget: Budget)
}

@Dao
interface DebtDao {
    @Query("SELECT * FROM debts WHERE isSettled = 0 ORDER BY dueDate")
    fun getActive(): Flow<List<Debt>>
    
    @Query("SELECT * FROM debts ORDER BY type, dueDate")
    fun getAll(): Flow<List<Debt>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(debt: Debt): Long
    
    @Update
    suspend fun update(debt: Debt)
    
    @Delete
    suspend fun delete(debt: Debt)
}