package com.example.myapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roommates")
data class Roommate(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
)