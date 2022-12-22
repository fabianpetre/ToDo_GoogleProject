package com.petref.finalproject

import android.R.attr.apiKey
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.petref.finalproject.database.ToDoData
import com.petref.finalproject.database.categories
import com.petref.finalproject.databinding.FragmentAddeditEntryBinding
import java.time.LocalDateTime
import java.util.*


class AddEditEntryFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private var isNewEntry : Boolean = true
    private var categoryChanged = false
    private var chosenCategoryPosition = 0
    private lateinit var binding : FragmentAddeditEntryBinding
    private lateinit var mToDoViewModel : ToDoViewModel
    private val calendar = Calendar.getInstance()
    private val args by navArgs<AddEditEntryFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the SDK
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY)

    }

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
        var taskLocation : String? = null
        // Spinner
        spinnerSetup()

        //Setting up the Auto Complete Places Widget
        autocompleteSetup()

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
        }

        binding.neDateTimeChecker.setOnClickListener {
            setupVisibilityConstraints()
        }

        binding.neTaskDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        //
        binding.neTaskTime.setOnClickListener {
            TimePickerDialog(requireContext(),
                this,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        binding.neAddLocationButton.setOnClickListener {
            binding.autocompleteFragment.visibility = View.VISIBLE
            val params = binding.neTitleHolder.layoutParams as ConstraintLayout.LayoutParams
            params.topToTop = ConstraintSet.UNSET
            params.topToBottom = binding.autocompleteFragment.id
        }

        // Submitting the entered values to the DB
        binding.neDoneButton.setOnClickListener {
            val title = binding.neTitle.text.toString()
            if(inputCheck(title)) {
                binding.neTitleHolder.error = null
                val details = binding.neDetails.text.toString()
                val taskTime = binding.neTaskTime.text.toString()
                val taskDate = binding.neTaskDate.text.toString()
                val timeCreated = setTimeString(timestamp)
                if (isNewEntry) { // Creating a new ToDoData item or saving changed data
                    val newToDoEntry = ToDoData(
                        0,
                        title = title,
                        category_position = chosenCategoryPosition,
                        details = details,
                        isBookmarkChecked = false,
                        isFinishedChecked = false,
                        taskDate = taskDate,
                        taskTime = taskTime,
                        timeCreated = timeCreated,
                        taskLocation = taskLocation
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
                            taskTime = taskTime,
                            taskDate = taskDate,
                            timeCreated = entryItem.timeCreated,
                            taskLocation = entryItem.taskLocation
                        )
                        mToDoViewModel.updateToDo(updatedUser)
                    }
                }

                findNavController().navigate(R.id.action_addEditEntryFragment_to_toDoListFragment)

            } else binding.neTitleHolder.error = "Title can't be empty"
        }

    }

    private fun autocompleteSetup() {
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                Log.d("Debugging", "Place: ${place.name}, ${place.latLng}")
                val params = binding.neTitleHolder.layoutParams as ConstraintLayout.LayoutParams
                params.topToBottom = ConstraintSet.UNSET
                params.topToTop = ConstraintSet.PARENT_ID
                binding.autocompleteFragment.visibility = View.GONE
                binding.neAddLocationButton.setCompoundDrawablesWithIntrinsicBounds(0,  0, R.drawable.ic_edit, 0)
                val latLng = place.latLng.toString().split(",")
                val lat = latLng[0]
                val lgt = latLng[1]
                Log.d("Debugging", "Lat: $lat, Log: $lgt")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.d("Debugging", "An error occurred: $status")
            }
        })
    }

    // Setups the Task Date and Task Time
    private fun setupVisibilityConstraints() {
//        binding.neTaskDate.isEnabled = !(binding.neTaskDate.isEnabled)
        binding.neTaskTime.paintFlags = binding.neTaskTime.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.neTaskDate.paintFlags = binding.neTaskDate.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.neDateTimeChecker.visibility = View.GONE
        binding.neTaskDate.text = "Task Date"
        binding.neTaskTime.text = "Task Time"
        binding.neTaskDate.visibility = View.VISIBLE
        binding.neTaskTime.visibility = View.VISIBLE
        val params = binding.neAddLocationButton.layoutParams as ConstraintLayout.LayoutParams
        params.topToBottom = binding.neTaskDate.id
    }

    private fun deleteToDo() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            if(args.entryItem != null) mToDoViewModel.deleteToDo(args.entryItem!!)
            findNavController().navigate(R.id.action_addEditEntryFragment_to_toDoListFragment)
        }
        builder.setNegativeButton("No"){ _, _ -> }
        builder.setTitle("Delete \"${args.entryItem?.title}?\"")
        builder.setMessage("Are you sure you want to delete this item?")
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
        if(!entryItem.taskDate.isNullOrBlank())
            setupVisibilityConstraints()
        binding.apply {
            neTitle.setText(entryItem.title)
            neDetails.setText(entryItem.details)
            neTaskTime.text = entryItem.taskTime
            neTaskDate.text = entryItem.taskDate
            neCategorySpinner.setText(categories[entryItem.category_position],false)
        }
    }

    // Setting / formatting the time to String
    private fun setTimeString(timeStamp: LocalDateTime): String {

        // Hours and minutes can look like 9:2 instead of 09:02 so we're formatting them right
        val minutes : String = if(timeStamp.minute<10) "0${timeStamp.minute}"
        else timeStamp.minute.toString()

        val hours : String = if(timeStamp.hour<10) "0${timeStamp.hour}"
        else timeStamp.hour.toString()

        // Formatting the month DECEMBER -> Dec
        val month = timeStamp.month.toString().lowercase().substring(0,3).replaceFirstChar{ it.uppercase() }

        // When creating a new item, will show "Today at 12:22" and in DB the full time will be saved
//        if (isNew) return "Today at ${timeStamp.hour}:$minutes"

        return "${timeStamp.dayOfMonth} $month at $hours:$minutes"
    }

    // Gets and sets current time by format
    private fun getTime() : LocalDateTime {
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

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val monthFormatted = when(month+1){
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            else -> "December"
        }
        binding.neTaskDate.text = "on $dayOfMonth $monthFormatted $year"
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val ampm : String
        val formattedHours : String
        if (hourOfDay>12){
            val tempHours = hourOfDay-12
            formattedHours = if(tempHours>9) "$tempHours" else "0$tempHours"
            ampm = "PM"
        }else if(hourOfDay==12) {
            formattedHours = "12"
            ampm = "PM"
        }
        else {
            formattedHours = if(hourOfDay>9) "$hourOfDay" else "0$hourOfDay"
            ampm = "AM"
        }
        val formattedMinutes = if(minute<10) "0$minute" else minute
        binding.neTaskTime.text = "at $formattedHours:$formattedMinutes $ampm"
    }
}
