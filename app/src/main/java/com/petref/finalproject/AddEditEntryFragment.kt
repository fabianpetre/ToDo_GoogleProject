package com.petref.finalproject

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.petref.finalproject.database.ToDoData
import com.petref.finalproject.database.categories
import com.petref.finalproject.databinding.FragmentAddeditEntryBinding
import java.util.*

class AddEditEntryFragment : Fragment() {
    private var isNewEntry : Boolean = true
    private var categoryChanged = false
    private var chosenCategoryPosition = 0
    private lateinit var binding : FragmentAddeditEntryBinding
//    private lateinit var mToDoViewModel : ToDoViewModel

    private val args by navArgs<AddEditEntryFragmentArgs>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddeditEntryBinding.inflate(inflater, container, false)
//        mToDoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)

        //Setting up the Entry if already existing
        val entryItem = args.entryItem
        if(entryItem != null) {
            isNewEntry = false
            setEntry(entryItem) // Setting up the existing data in the entry and flagging the entry as already existing
        }
        else binding.neTimeCreated.text = getSetTime() // If item is new, get and display task creation time
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setting up Delete Menu
        if(!isNewEntry) {
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
        }

        //Getting the time of entering in NewEntryFragment
        val timestamp = getSetTime()

        // Spinner
        spinnerSetup()

        val adapter = ToDoAdapter()

        binding.neDoneButton.setOnClickListener {
            val title = binding.neTitle.text.toString()
            if(inputCheck(title)) {
                binding.neTitleHolder.error = null
                val details = binding.neDetails.text.toString()

                if (isNewEntry) { // Creating a new ToDoData item or saving changed data
                    val rvPosition = toDoList.size - 1
                    val newToDoEntry = ToDoData(
                        0,
                        title = title,
                        category_position = chosenCategoryPosition,
                        rv_position = rvPosition,
                        details = details,
                        isBookmarkChecked = false,
                        isFinishedChecked = false,
                        timeStamp = timestamp
                    )
                    toDoList.add(newToDoEntry)
//                    mToDoViewModel.addToDo(newToDoEntry)
                    adapter.notifyItemInserted(rvPosition)
                }
                else { // Save changed data
                    val rvPosition = args.entryItem!!.rv_position
                    val item = toDoList[rvPosition]
                    item.title = title
                    item.details = details
                    if(categoryChanged) item.category_position = chosenCategoryPosition
                }

                findNavController().navigate(R.id.action_addEditEntryFragment_to_toDoListFragment)

            } else binding.neTitleHolder.error = "Title can't be empty"
        }
    }

    private fun deleteToDo() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            //TODO implement ROOM delete
            toDoList.remove(args.entryItem)
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

    private fun setEntry(entryItem : ToDoData){
        binding.apply {
            neTitle.setText(entryItem.title)
            neDetails.setText(entryItem.details)
            neTimeCreated.text = entryItem.timeStamp
            neCategorySpinner.setText(categories[entryItem.category_position],false)
        }
    }

    private fun getSetTime() : String {
        val c = Calendar.getInstance()
        return "${c.get(Calendar.DAY_OF_MONTH)} at ${c.get(Calendar.HOUR_OF_DAY)}:${c.get(Calendar.MINUTE)}"
    }

}
