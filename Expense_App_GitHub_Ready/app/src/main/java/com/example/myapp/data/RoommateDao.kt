package com.example.myapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface RoommateDao {
    @Query("SELECT * FROM roommates")
    fun getAllRoommates(): List<Roommate>

    @Insert
    fun insert(roommate: Roommate): Long

    @Update
    fun update(roommate: Roommate)

    @Delete
    fun delete(roommate: Roommate)
}