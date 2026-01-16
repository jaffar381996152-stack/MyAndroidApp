package com.example.myapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val buyerId: Long,
    val price: Double,
    val itemName: String,
    val date: Long = System.currentTimeMillis()
)