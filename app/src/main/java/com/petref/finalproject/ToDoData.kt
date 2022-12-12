package com.petref.finalproject

data class ToDoData (
    val title : String,
    val category : String,
    val details : String,
    val isBookmarkChecked : Boolean,
    val isFinishedChecked : Boolean,
    val timeStamp : String
    )

val categories = arrayOf("School", "Work", "Family")