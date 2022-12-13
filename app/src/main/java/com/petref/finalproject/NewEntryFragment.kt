package com.petref.finalproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.petref.finalproject.databinding.FragmentNewEntryBinding
import java.util.*

class NewEntryFragment : Fragment() {
    var isNewEntry : Boolean = true
    val args : NewEntryFragmentArgs by navArgs()
    private lateinit var binding : FragmentNewEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewEntryBinding.inflate(inflater, container, false)
        val bundle = arguments
        if (bundle != null) {
            val args = NewEntryFragmentArgs.fromBundle(bundle)
            if(args.entryItem?.title?.isNotBlank() == true && args.entryItem?.title?.isNotEmpty() == true) {
                isNewEntry = false
                val entryItem = args.entryItem
                binding.apply {
                    if (entryItem != null) {
                        neTitle.setText(entryItem.title)
                        neDetails.setText(entryItem.details)
                        neTimeCreated.text = entryItem.timeStamp
                        //neCategorySpinner.setSelection(entryItem.chosen_category_position)
                    }
                }
            } else {
                val c = Calendar.getInstance()
                val timestamp = "${c.get(Calendar.DAY_OF_MONTH)} at ${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}"
                binding.neTimeCreated.text = timestamp
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lateinit var chosenCategory : String
        var chosenCategoryPosition = 0
        val c = Calendar.getInstance()
        val timestamp = "${c.get(Calendar.DAY_OF_MONTH)} at ${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}"

        val adapter = ToDoAdapter(toDoList){_ ->}

        val spinner = binding.neCategorySpinner
        val spinnerAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.setAdapter(spinnerAdapter)

        spinner.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            chosenCategory = categories[position]
            chosenCategoryPosition = position
        }

        binding.neDoneButton.setOnClickListener {
            val title = binding.neTitle.text.toString()
            val details = binding.neDetails.text.toString()
            if(title.isBlank() || title.isEmpty()){ binding.neTitleHolder.error = "Title can't be empty" }
            else {
                binding.neTitleHolder.error = null
                if (isNewEntry) {

                    val rvPosition = toDoList.size - 1
                    val newToDoEntry = ToDoData(
                        title = title,
                        category = chosenCategory,
                        chosen_category_position = chosenCategoryPosition,
                        rv_position = rvPosition,
                        details = details,
                        false,
                        false,
                        timestamp
                    )
                    toDoList.add(newToDoEntry)
                    adapter.notifyItemInserted(rvPosition)
                } else {
                    val rvPosition = args.entryItem!!.rv_position
                    val item = toDoList[rvPosition]
                    item.title = title
                    item.details = binding.neDetails.text.toString()
                    item.category = categories[chosenCategoryPosition]
                }
                findNavController().navigate(R.id.action_newEntryFragment_to_toDoListFragment2)
            }
        }

    }
}