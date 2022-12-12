package com.petref.finalproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.petref.finalproject.databinding.FragmentNewEntryBinding
import java.util.*

class NewEntryFragment : Fragment() {


    private lateinit var binding : FragmentNewEntryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewEntryBinding.inflate(inflater, container, false)
        val c = Calendar.getInstance()
        binding.neTimeCreated.text = "${c.get(Calendar.DAY_OF_MONTH).toString()} at ${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}"
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lateinit var chosenCategory : String
        val adapter = ToDoAdapter(toDoList)

        val spinner = binding.neCategorySpinner
        val spinnerAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.setAdapter(spinnerAdapter)

        spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id -> chosenCategory = categories[position] }
//
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) { chosenCategory = categories[position] }
//            override fun onNothingSelected(p0: AdapterView<*>?) { }
//        }


        binding.neAddButon.setOnClickListener {
            val title = binding.neTitle.text.toString()
            if(title.isBlank() || title.isEmpty()){
                binding.neTitleHolder.error = "Title can't be empty"
            } else {
                binding.neTitleHolder.error = null
                val details = binding.neDetails.text.toString()
                val newToDoEntry =
                    ToDoData(title, chosenCategory, details, false, false, "Today at 17:30")

                toDoList.add(newToDoEntry)
                adapter.notifyItemInserted(toDoList.size - 1)

                findNavController().navigate(R.id.action_newEntryFragment_to_toDoListFragment2)
            }
        }

    }
}