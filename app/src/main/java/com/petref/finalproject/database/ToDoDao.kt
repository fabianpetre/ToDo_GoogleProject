package com.petref.finalproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ToDoDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToDo(toDo : ToDoData)

    @Query("SELECT * FROM todo_table ORDER BY id")
    fun readAllData() : LiveData<List<ToDoData>>
}