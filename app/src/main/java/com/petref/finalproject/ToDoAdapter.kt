package com.petref.finalproject

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.petref.finalproject.databinding.TodoItemBinding

class ToDoAdapter(
    private val toDoList: MutableList<ToDoData>,
    private val listener: (ToDoData) -> Unit) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

    class ToDoViewHolder(val binding : TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val item = toDoList[position]
        item.rv_position = position

        holder.binding.apply {
            tdTitle.text = toDoList[position].title
            tdCategory.text = categories[toDoList[position].category_position]
            tdIsDone.isChecked = toDoList[position].isFinishedChecked
            tdDateCreated.text=toDoList[position].timeStamp
            tdButtonFavourite.setImageResource(
                if(!toDoList[position].isBookmarkChecked)
                R.drawable.ic_bookmark_empty else R.drawable.ic_bookmark_full)
        }
        holder.binding.itemViewGroup.setOnClickListener { listener(item) }

    }

    override fun getItemCount(): Int {
        return toDoList.size
    }
}