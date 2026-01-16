package com.example.myapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "month_summaries")
data class MonthSummary(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val month: Int,
    val year: Int,
    val totalExpenses: Double,
    val roommatesCount: Int,
    val dues: String // JSON
)