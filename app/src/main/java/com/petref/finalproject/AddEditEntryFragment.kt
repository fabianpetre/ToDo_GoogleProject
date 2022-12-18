package com.petref.finalproject

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.petref.finalproject.database.ToDoData
import com.petref.finalproject.database.ToDoViewModel
import com.petref.finalproject.database.categories
import com.petref.finalproject.databinding.FragmentAddeditEntryBinding
import java.time.LocalDateTime
import java.util.*

class AddEditEntryFragment : Fragment() {
    private var isNewEntry : Boolean = true
    private var categoryChanged = false
    private var chosenCategoryPosition = 0
    private lateinit var binding : FragmentAddeditEntryBinding
    private lateinit var mToDoViewModel : ToDoViewModel

    private val args by navArgs<AddEditEntryFragmentArgs>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddeditEntryBinding.inflate(inflater, container, false)
        mToDoViewModel = ViewModelProvider(this)[ToDoViewModel::class.java]

        //Setting up the Entry if already existing
        val entryItem = args.entryItem
        if(entryItem != null) {
            isNewEntry = false
            setEntry(entryItem) // Setting up the existing data in the entry and flagging the entry as already existing
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Getting the time of entering in AddEditEntryFragment
        val timestamp = getTime()

        // Spinner
        spinnerSetup()

        // Setting up Delete Menu
        // Getting and setting time of new task creation
        if(!isNewEntry) {
            // Setting up Delete Menu
            (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.delete_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    if(menuItem.itemId == R.id.menu_delete){
                        deleteToDo()
                        return true
                    }
                    return false
                }
            }, viewLifecycleOwner)

            // Getting and setting time of new task creation
        } else {
            binding.neTimeCreated.text = setTimeString(timestamp, true)
        }

        binding.neDoneButton.setOnClickListener {
            val title = binding.neTitle.text.toString()
            if(inputCheck(title)) {
                binding.neTitleHolder.error = null
                val details = binding.neDetails.text.toString()

                if (isNewEntry) { // Creating a new ToDoData item or saving changed data
                    val newToDoEntry = ToDoData(
                        0,
                        title = title,
                        category_position = chosenCategoryPosition,
                        details = details,
                        isBookmarkChecked = false,
                        isFinishedChecked = false,
                        timeStamp = setTimeString(timestamp, false)
                    )
                    mToDoViewModel.addToDo(newToDoEntry)
                }
                else { // Save changed data
                    val entryItem = args.entryItem
                    if (entryItem != null) {
                        var categoryPosition = entryItem.category_position
                        if (categoryChanged) categoryPosition = chosenCategoryPosition
                        val updatedUser = ToDoData(
                            id = entryItem.id,
                            title = title,
                            category_position = categoryPosition,
                            details = details,
                            isBookmarkChecked = entryItem.isBookmarkChecked,
                            isFinishedChecked = entryItem.isFinishedChecked,
                            timeStamp = entryItem.timeStamp
                        )
                        mToDoViewModel.updateToDo(updatedUser)
                    }
                }

                findNavController().navigate(R.id.action_addEditEntryFragment_to_toDoListFragment)

            } else binding.neTitleHolder.error = "Title can't be empty"
        }
    }

    private fun deleteToDo() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
                if(args.entryItem != null)
                mToDoViewModel.deleteToDo(args.entryItem!!)
            findNavController().navigate(R.id.action_addEditEntryFragment_to_toDoListFragment)
        }
        builder.setNegativeButton("No"){ _, _ -> }
        builder.setTitle("Delete ${args.entryItem?.title}?")
        builder.setMessage("Are you sure you want to delete this ToDo item?")
        builder.create().show()
    }

    private fun spinnerSetup() {
        val spinner = binding.neCategorySpinner
        val spinnerAdapter = ArrayAdapter(requireView().context, android.R.layout.simple_spinner_dropdown_item, categories)
        spinner.setAdapter(spinnerAdapter)
        if (isNewEntry) spinner.setText(categories[chosenCategoryPosition], false)
        spinner.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            chosenCategoryPosition = position
            categoryChanged = true
        }
    }

    private fun inputCheck(title : String): Boolean { return !(title.isBlank() || title.isEmpty()) }

    // Sets up the entry with already existing data
    private fun setEntry(entryItem : ToDoData){
        binding.apply {
            neTitle.setText(entryItem.title)
            neDetails.setText(entryItem.details)
            neTimeCreated.text = entryItem.timeStamp
            neCategorySpinner.setText(categories[entryItem.category_position],false)
        }
    }

    private fun setTimeString(timeStamp: LocalDateTime, isNew : Boolean): String {
        if (isNew)
            return "Today at ${timeStamp.hour}:${timeStamp.minute}"
        return "${timeStamp.dayOfMonth} ${timeStamp.month} at ${timeStamp.hour}:${timeStamp.minute}"
    }

    // Gets and sets current time by format
    private fun getTime() : LocalDateTime {
        val calendar = Calendar.getInstance()
        val currentTimeStamp = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )
        return currentTimeStamp
    }


}
