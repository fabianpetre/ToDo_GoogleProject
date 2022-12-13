package com.petref.finalproject

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToDoData (
    var title : String,
    var category : String,
    var chosen_category_position : Int,
    var rv_position : Int,
    var details : String = "",
    var isBookmarkChecked : Boolean,
    var isFinishedChecked : Boolean,
    var timeStamp : String
    ) : Parcelable

val categories = arrayOf("School", "Work", "Family")