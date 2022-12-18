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

    private lateinit var binding: FragmentToDoListBinding
    private lateinit var mToDoViewModel: ToDoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentToDoListBinding.inflate(inflater, container, false)
        mToDoViewModel = ViewModelProvider(this)[ToDoViewModel::class.java]

        val adapter = ToDoAdapter(mToDoViewModel)
        binding.todolistRecyclerView.adapter = adapter
        binding.todolistRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Keeps the list updated
        mToDoViewModel.readAllData.observe(viewLifecycleOwner, Observer { ToDoData ->
            mToDoViewModel.itemsCount.value= ToDoData.size
            adapter.setData(ToDoData)
        })

        // Keeps the items count updated
        mToDoViewModel.itemsCount.observe(viewLifecycleOwner, Observer { itemsCount ->
            binding.todolistItemCount.text = getString(R.string.ne_itemCount, itemsCount.toString())
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.todolistFab.setOnClickListener {
            val action =
                ToDoListFragmentDirections.actionToDoListFragmentToAddEditEntryFragment(null)
            Navigation.findNavController(view).navigate(action)
        }
    }
}