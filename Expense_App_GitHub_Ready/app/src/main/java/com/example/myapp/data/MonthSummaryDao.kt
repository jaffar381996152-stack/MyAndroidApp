package com.example.myapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MonthSummaryDao {
    @Query("SELECT * FROM month_summaries ORDER BY year DESC, month DESC")
    fun getAllSummaries(): List<MonthSummary>

    @Insert
    fun insert(summary: MonthSummary): Long
}