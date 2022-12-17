package com.petref.finalproject

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.petref.finalproject.database.ToDoData
import com.petref.finalproject.database.categories
import com.petref.finalproject.databinding.TodoItemBinding

class ToDoAdapter(): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

//    private var todoList = emptyList<ToDoData>()

    class ToDoViewHolder(val binding : TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int { return toDoList.size }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
//        val currentItem = todoList[position]
//        currentItem.rv_position = position
//
//        holder.binding.apply {
//            tdTitle.text = currentItem.title
//            tdCategory.text = categories[currentItem.category_position]
//            tdIsDone.isChecked = currentItem.isFinishedChecked
//            tdDateCreated.text=currentItem.timeStamp
//            tdButtonFavourite.setImageResource(
//                if(!currentItem.isBookmarkChecked)
//                R.drawable.ic_bookmark_empty else R.drawable.ic_bookmark_full)
//        }
//        holder.binding.itemViewGroup.setOnClickListener {
//            val action = ToDoListFragmentDirections.actionToDoListFragmentToAddEditEntryFragment(currentItem)
//            holder.binding.itemViewGroup.findNavController().navigate(action)
//        }

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
        holder.binding.itemViewGroup.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToAddEditEntryFragment(item)
            holder.binding.itemViewGroup.findNavController().navigate(action)
        }
    }

//    fun setData(toDo : List<ToDoData>){
//        this.todoList = toDo
//        notifyDataSetChanged()
//    }
}