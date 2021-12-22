package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentRegistration1Binding
import com.project.loveis.databinding.FragmentRegistration3Binding

class Registration3Fragment : Fragment(R.layout.fragment_registration3) {
    private val binding: FragmentRegistration3Binding by viewBinding()
    private val args: Registration3FragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(
                Registration3FragmentDirections.actionRegistration3FragmentToRegistration4Fragment(
                    phone = args.phone,
                    gender = args.gender,
                    photo = args.photo,
                    name = binding.nameEditText.text.toString()
                )
            )
        }

    }
}