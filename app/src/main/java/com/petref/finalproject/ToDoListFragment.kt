package com.petref.finalproject

import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.petref.finalproject.databinding.FragmentToDoListBinding

class ToDoListFragment : Fragment() {


    private lateinit var binding : FragmentToDoListBinding
    private lateinit var viewModel: ToDoListViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentToDoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ToDoAdapter(toDoList){

            toDoData ->
            val action = ToDoListFragmentDirections.actionToDoListFragmentToNewEntryFragment(toDoData)
            Navigation.findNavController(view).navigate(action)
        }

        binding.todolistRecyclerView.adapter = adapter
        binding.todolistRecyclerView.layoutManager = LinearLayoutManager(view.context)

        val buttonFAB = binding.todolistFab

        buttonFAB.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToNewEntryFragment(null)
            Navigation.findNavController(view).navigate(action)
        }

        recyclerView = binding.todolistRecyclerView

    }
}