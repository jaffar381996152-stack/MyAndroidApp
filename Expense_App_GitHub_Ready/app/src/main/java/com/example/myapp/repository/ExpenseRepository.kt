package com.example.myapp.repository

import com.example.myapp.data.AppDatabase
import com.example.myapp.data.Expense
import com.example.myapp.data.Roommate
import com.example.myapp.data.MonthSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExpenseRepository(private val database: AppDatabase) {
    fun getAllRoommates(): Flow<List<Roommate>> = flow {
        emit(database.roommateDao().getAllRoommates())
    }

    fun getAllExpenses(): Flow<List<Expense>> = flow {
        emit(database.expenseDao().getAllExpenses())
    }

    fun getAllSummaries(): Flow<List<MonthSummary>> = flow {
        emit(database.monthSummaryDao().getAllSummaries())
    }

    suspend fun insertRoommate(roommate: Roommate) {
        database.roommateDao().insert(roommate)
    }

    suspend fun insertExpense(expense: Expense) {
        database.expenseDao().insert(expense)
    }

    suspend fun insertSummary(summary: MonthSummary) {
        database.monthSummaryDao().insert(summary)
    }

    suspend fun clearExpenses() {
        database.expenseDao().clearAll()
    }
}