package com.petref.finalproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.petref.finalproject.databinding.TodoItemBinding

class ToDoAdapter ( var todo : List<ToDoData>) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

    inner class ToDoViewHolder(val binding : TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return ToDoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.binding.apply {
            tdTitle.text = todo[position].title
            tdCategory.text = todo[position].category
            tdIsDone.isChecked = todo[position].isFinishedChecked
            tdButtonFavourite.setImageResource(
                if(!todo[position].isBookmarkChecked)
                R.drawable.ic_bookmark_empty else R.drawable.ic_bookmark_full)
            tdDateCreated.text=todo[position].timeStamp
        }
    }

    override fun getItemCount(): Int {
        return todo.size
    }
}