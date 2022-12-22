package com.petref.finalproject.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "todo_table")
data class ToDoData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "category_position") var category_position: Int,
    @ColumnInfo(name = "details") var details: String = "",
    @ColumnInfo(name = "is_bookmark_checked") var isBookmarkChecked: Boolean = false,
    @ColumnInfo(name = "is_finished_checked") var isFinishedChecked: Boolean = false,
    @ColumnInfo(name = "time_created") var timeCreated: String,
    @ColumnInfo(name = "task_date") var taskDate: String?,
    @ColumnInfo(name = "task_time") var taskTime: String?,
    @ColumnInfo(name = "task_location") var taskLocation: String?
) : Parcelable

var categories = arrayOf("Day-to-Day", "School", "Work", "Family","Hobbies")
var languages = arrayOf("English", "Deutsch")