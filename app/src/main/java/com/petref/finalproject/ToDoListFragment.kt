package com.petref.finalproject

import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.petref.finalproject.database.ToDoData
import com.petref.finalproject.database.ToDoViewModel
import com.petref.finalproject.databinding.FragmentToDoListBinding

class ToDoListFragment : Fragment() {

    private lateinit var binding : FragmentToDoListBinding
//    private lateinit var viewModelToDo: ToDoListViewModel
//    private lateinit var viewModelDatabase: ToDoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//      ToDoViewModel
//        viewModelToDo = ViewModelProvider(this).get(ToDoListViewModel::class.java)
//        viewModelToDo.currentItemsNumber.observe(viewLifecycleOwner,Observer{
//            binding.todolistItemCount.text = getString(R.string.ne_itemCount, it.toString())
//        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentToDoListBinding.inflate(inflater, container, false)

        val adapter = ToDoAdapter()
//        viewModelToDo.currentItemsNumber.value = toDoList.size
        binding.todolistRecyclerView.adapter = adapter
        binding.todolistRecyclerView.layoutManager = LinearLayoutManager(requireContext())

//        viewModelDatabase = ViewModelProvider(this).get(ToDoViewModel::class.java)
//        viewModelDatabase.readAllData.observe(viewLifecycleOwner, Observer{ ToDoData ->
//            adapter.setData(ToDoData)
//        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todolistFab.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToAddEditEntryFragment(null)
            Navigation.findNavController(view).navigate(action)
        }
    }
}