package com.example.myapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): List<Expense>

    @Insert
    fun insert(expense: Expense): Long

    @Query("DELETE FROM expenses")
    fun clearAll()
}