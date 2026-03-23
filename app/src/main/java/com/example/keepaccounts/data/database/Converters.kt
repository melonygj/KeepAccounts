package com.example.keepaccounts.data.database

import androidx.room.TypeConverter
import com.example.keepaccounts.data.model.*
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromTransactionType(value: TransactionType): String {
        return value.name
    }

    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return try { TransactionType.valueOf(value) } catch (e: Exception) { TransactionType.EXPENSE }
    }

    @TypeConverter
    fun fromAccountType(value: AccountType): String {
        return value.name
    }

    @TypeConverter
    fun toAccountType(value: String): AccountType {
        return try { AccountType.valueOf(value) } catch (e: Exception) { AccountType.CASH }
    }

    @TypeConverter
    fun fromBudgetPeriod(value: BudgetPeriod): String {
        return value.name
    }

    @TypeConverter
    fun toBudgetPeriod(value: String): BudgetPeriod {
        return try { BudgetPeriod.valueOf(value) } catch (e: Exception) { BudgetPeriod.MONTHLY }
    }

    @TypeConverter
    fun fromDebtType(value: DebtType): String {
        return value.name
    }

    @TypeConverter
    fun toDebtType(value: String): DebtType {
        return try { DebtType.valueOf(value) } catch (e: Exception) { DebtType.BORROW }
    }
}