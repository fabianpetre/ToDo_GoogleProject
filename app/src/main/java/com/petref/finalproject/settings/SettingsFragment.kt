package com.petref.finalproject.settings

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.petref.finalproject.R
import com.petref.finalproject.database.categories
import com.petref.finalproject.database.languages
import com.petref.finalproject.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding : FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val langSpinner = binding.languagesSpinner
        val spinnerAdapter = ArrayAdapter(requireView().context, android.R.layout.simple_spinner_dropdown_item, languages)
        langSpinner.setAdapter(spinnerAdapter)
        langSpinner.onItemClickListener = AdapterView.OnItemClickListener{ parent, view, position, id ->
            val selectedLanguage = categories[position]
            when(selectedLanguage){
                "English" -> setLocale("en")
                else -> setLocale("de")
            }
        }
    }

    private fun setLocale(language: String) {
        val resources = resources
        val metrics = resources.displayMetrics
        val configuration = resources.configuration
    }
}