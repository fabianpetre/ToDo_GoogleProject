package com.petref.finalproject

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToDoListViewModel : ViewModel() {

    val currentItemsNumber : MutableLiveData<Int> by lazy{
        MutableLiveData<Int>()
    }
}