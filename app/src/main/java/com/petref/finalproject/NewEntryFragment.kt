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
    private var isNewEntry : Boolean = true
    private var categoryChanged = false
    private val args : NewEntryFragmentArgs by navArgs()
    private lateinit var binding : FragmentNewEntryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewEntryBinding.inflate(inflater, container, false)
        val bundle = arguments
        /**
         * Setting up the Entry if already existing
         */
        if (bundle != null) {
            val args = NewEntryFragmentArgs.fromBundle(bundle)
            val entryItem = args.entryItem
            if(entryItem != null) { //Checking if the entry item is null
                if (entryItem.title.isNotBlank() && entryItem.title.isNotEmpty()) {
                    isNewEntry = false
                    setEntry(entryItem) // Setting up the existing data in the entry and flagging the entry as already existing
                }
            }
            // If item is new, get and display task creation time
            else {
                val c = Calendar.getInstance()
                val timestamp = "${c.get(Calendar.DAY_OF_MONTH)} at ${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}"
                binding.neTimeCreated.text = timestamp
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var chosenCategoryPosition = 0

        //Getting the time of entering in NewEntryFragment
        val c = Calendar.getInstance()
        val timestamp = "${c.get(Calendar.DAY_OF_MONTH)} at ${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}"

        val adapter = ToDoAdapter(toDoList){_ ->}
        val spinner = binding.neCategorySpinner
        val spinnerAdapter = ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.setAdapter(spinnerAdapter)

        if (isNewEntry) spinner.setText(categories[chosenCategoryPosition], false)

        spinner.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            chosenCategoryPosition = position
            categoryChanged = true
        }

        binding.neDoneButton.setOnClickListener {
            val title = binding.neTitle.text.toString()
            val details = binding.neDetails.text.toString()
            if(title.isBlank() || title.isEmpty()) binding.neTitleHolder.error = "Title can't be empty"
            else {
                binding.neTitleHolder.error = null
                // Creating a new ToDoData item or saving changed data
                if (isNewEntry) {
                    val rvPosition = toDoList.size - 1
                    val newToDoEntry = ToDoData(
                        title = title,
                        category_position = chosenCategoryPosition,
                        rv_position = rvPosition,
                        details = details,
                        isBookmarkChecked = false,
                        isFinishedChecked = false,
                        timeStamp = timestamp
                    )
                    toDoList.add(newToDoEntry)
                    adapter.notifyItemInserted(rvPosition)
                } else {
                    val rvPosition = args.entryItem!!.rv_position
                    val item = toDoList[rvPosition]
                    item.title = title
                    item.details = binding.neDetails.text.toString()
                    if(categoryChanged) item.category_position = chosenCategoryPosition
                }
                findNavController().navigate(R.id.action_newEntryFragment_to_toDoListFragment2)
            }
        }
    }

    private fun setEntry(entryItem : ToDoData){
        binding.apply {
            neTitle.setText(entryItem.title)
            neDetails.setText(entryItem.details)
            neTimeCreated.text = entryItem.timeStamp
            neCategorySpinner.setText(categories[entryItem.category_position],false)
        }
    }
}
