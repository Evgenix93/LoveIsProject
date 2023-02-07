package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentAddPlace1Binding

class CreatePlaceFragment1: Fragment(R.layout.fragment_add_place1) {
    private val binding: FragmentAddPlace1Binding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initContinueButton()

    }

    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.create_place)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun initContinueButton(){
        binding.continueBtn.setOnClickListener {
            if (binding.nameEditText.text.isBlank())
              Toast.makeText(requireContext(), "Укажите название", Toast.LENGTH_SHORT).show()
            else
               findNavController().navigate(CreatePlaceFragment1Directions.actionCreatePlaceFragment1ToCreatePlaceFragment2(
                binding.nameEditText.text.toString()
            ))
        }
    }
}