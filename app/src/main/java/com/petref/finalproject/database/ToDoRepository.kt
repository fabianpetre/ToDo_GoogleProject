package com.petref.finalproject.database

import androidx.lifecycle.LiveData

class ToDoRepository(private val todoDao : ToDoDao) {
    val readAllData: LiveData<List<ToDoData>> = todoDao.readAllData()

    suspend fun addToDo(todo : ToDoData){
        todoDao.addToDo(todo)
    }
}