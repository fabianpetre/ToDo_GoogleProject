package com.petref.finalproject.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoDao {

    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToDo(toDo : ToDoData)

    @Update
    suspend fun updateToDo(toDo : ToDoData)

    @Delete
    suspend fun deleteToDo(toDo : ToDoData)

    @Query("SELECT * FROM todo_table ORDER BY id")
    fun readAllData() : LiveData<List<ToDoData>>
}