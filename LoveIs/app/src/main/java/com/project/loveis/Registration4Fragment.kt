package com.project.loveis

import android.os.Bundle
import android.view.View
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

    private fun showChoosenDate() {

    }

    private fun showError() {

    }
}