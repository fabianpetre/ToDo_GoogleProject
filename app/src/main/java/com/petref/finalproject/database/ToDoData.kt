package com.petref.finalproject.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "todo_table")
data class ToDoData (
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var title : String,
    var category_position : Int,
    var rv_position : Int,
    var details : String = "",
    var isBookmarkChecked : Boolean,
    var isFinishedChecked : Boolean,
    var timeStamp : String
    ) : Parcelable

var categories = arrayOf("School", "Work", "Family")