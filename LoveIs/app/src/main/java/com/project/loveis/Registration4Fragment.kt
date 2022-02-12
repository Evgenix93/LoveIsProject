package com.project.loveis

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentRegistration1Binding
import com.project.loveis.databinding.FragmentRegistration4Binding
import java.util.*

class Registration4Fragment : Fragment(R.layout.fragment_registration4) {
    private val binding: FragmentRegistration4Binding by viewBinding()
    private val args: Registration4FragmentArgs by navArgs()
    private var date = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
        initEditText()
    }

    private fun setOnClickListener() {
        binding.continueBtn.setOnClickListener {
            if (binding.dayEditText.text.isNullOrBlank() || binding.monthEditText.text.isNullOrBlank() || binding.yearEditText.text.isNullOrBlank()) {
                showError()
            } else {
                date.set(
                    binding.yearEditText.text.toString().toInt(),
                    binding.monthEditText.text.toString().toInt(),
                    binding.dayEditText.text.toString().toInt()
                )
                findNavController().navigate(
                    Registration4FragmentDirections.actionRegistration4FragmentToRegistration5Fragment(
                        phone = args.phone,
                        gender = args.gender,
                        photo = args.photo,
                        name = args.name,
                        birthDate = date.timeInMillis
                    )
                )
            }
        }


    }

    private fun initEditText(){
        val days = List(31){
            val day = it + 1
            if(day < 10)
                "0$day"
            else
                "$day"
        }
        val adapterDays = ArrayAdapter<String>(requireContext(), R.layout.support_simple_spinner_dropdown_item, days)
        binding.dayEditText.setAdapter(adapterDays)
        binding.dayEditText.setOnClickListener { binding.dayEditText.showDropDown() }
        val months = List(12){
            val month = it + 1
            if(month < 10)
                "0$month"
            else
                "$month"
        }
        val adapterMonths = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, months)
        binding.monthEditText.setAdapter(adapterMonths)
        binding.monthEditText.setOnClickListener{
            binding.monthEditText.showDropDown()
        }
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = List(currentYear + 1 - 1930){(1930 + it).toString()}
        val adapterYears = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item, years)
        binding.yearEditText.setAdapter(adapterYears)
        binding.yearEditText.setOnClickListener{
            binding.yearEditText.showDropDown()
        }
      //  binding.dayEditText.setOnFocusChangeListener { view, focused ->
          //  if(focused) {
              //  binding.dayEditText.compoundDrawables.set(3, ResourcesCompat.getDrawable(resources, R.drawable.ic_line_4, null))
        //    }
    //    }
    }

    private fun showChoosenDate() {

    }

    private fun showError() {

    }
}