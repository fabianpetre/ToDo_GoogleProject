package com.petref.finalproject.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel (application: Application): AndroidViewModel(application){

    val readAllData: LiveData<List<ToDoData>>
    private val repository : ToDoRepository

    init {
        val todoDao = ToDoDatabase.getDatabase(application).ToDoDao()
        repository = ToDoRepository(todoDao)
        readAllData = repository.readAllData
    }

    fun addToDo(todo : ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.addToDo(todo)
        }
    }

}