package com.petref.finalproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.petref.finalproject.database.ToDoData
import com.petref.finalproject.database.categories
import com.petref.finalproject.databinding.TodoItemBinding

class ToDoAdapter(private var mToDoViewModel : ToDoViewModel): RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>(){

    private var todoList = emptyList<ToDoData>()
    class ToDoViewHolder(val binding : TodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TodoItemBinding.inflate(layoutInflater, parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int { return todoList.size }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val currentItem = todoList[position]

        // Binding information to item
        holder.binding.apply {
            tdTitle.text = currentItem.title
            tdCategory.text = categories[currentItem.category_position]
            tdIsDone.isChecked = currentItem.isFinishedChecked
            tdDateCreated.text=currentItem.timeStamp
            tdButtonFavourite.setImageResource(
                if(!currentItem.isBookmarkChecked)
                R.drawable.ic_bookmark_empty else R.drawable.ic_bookmark_full)
        }

        // If an item is pressed, goes to AddEditFragment with existing information
        holder.binding.itemViewGroup.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToAddEditEntryFragment(currentItem)
            holder.binding.itemViewGroup.findNavController().navigate(action)
        }

        // Updating the item if Favourite button is pressed
        holder.binding.tdButtonFavourite.setOnClickListener{
            val updatedItem = ToDoData(
                id = currentItem.id,
                title = currentItem.title,
                category_position = currentItem.category_position,
                details = currentItem.details,
                isBookmarkChecked = !currentItem.isBookmarkChecked,
                isFinishedChecked = currentItem.isFinishedChecked,
                timeStamp = currentItem.timeStamp
            )
            mToDoViewModel.updateToDo(updatedItem)
        }

        // Updating the item if Done button is pressed
        holder.binding.tdIsDone.setOnClickListener {
            val updatedItem = ToDoData(
                id = currentItem.id,
                title = currentItem.title,
                category_position = currentItem.category_position,
                details = currentItem.details,
                isBookmarkChecked = currentItem.isBookmarkChecked,
                isFinishedChecked = !currentItem.isFinishedChecked,
                timeStamp = currentItem.timeStamp
            )
            mToDoViewModel.updateToDo(updatedItem)
        }
    }

    fun setData(toDo : List<ToDoData>){
        this.todoList = toDo
        notifyDataSetChanged()
    }
}