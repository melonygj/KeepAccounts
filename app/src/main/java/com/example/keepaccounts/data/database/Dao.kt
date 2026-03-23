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
    
    @Delete
    suspend fun delete(category: Category)
}

@Dao
interface AccountDao {
    @Query("SELECT * FROM accounts ORDER BY name")
    fun getAll(): Flow<List<Account>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account): Long
    
    @Update
    suspend fun update(account: Account)
    
    @Delete
    suspend fun delete(account: Account)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAll(): Flow<List<Transaction>>
    
    @Query("SELECT * FROM transactions WHERE date BETWEEN :startTime AND :endTime ORDER BY date DESC")
    fun getByDateRange(startTime: Long, endTime: Long): Flow<List<Transaction>>
    
    @Insert
    suspend fun insert(transaction: Transaction): Long
    
    @Delete
    suspend fun delete(transaction: Transaction)
    
    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type AND date BETWEEN :startTime AND :endTime")
    fun getTotalByType(type: TransactionType, startTime: Long, endTime: Long): Flow<Double?>
}