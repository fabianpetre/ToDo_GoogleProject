package com.petref.finalproject

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.petref.finalproject.database.ToDoData
import com.petref.finalproject.database.ToDoDatabase
import com.petref.finalproject.database.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel (application: Application): AndroidViewModel(application){

    val readAllData: LiveData<List<ToDoData>>
    val itemsCount: MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }
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

    fun updateToDo(todo : ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateToDo(todo)
        }
    }

    fun deleteToDo(todo : ToDoData){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteToDo(todo)
        }
    }

}