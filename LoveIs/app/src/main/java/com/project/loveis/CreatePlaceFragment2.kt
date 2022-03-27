package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentAddPlace1Binding

class CreatePlaceFragment2: Fragment(R.layout.fragment_add_place1) {
    private val binding: FragmentAddPlace1Binding by viewBinding()
    private val args: CreatePlaceFragment2Args by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initContinueButton()
        initViews()

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
            findNavController().navigate(CreatePlaceFragment2Directions.actionCreatePlaceFragment2ToCreatePlaceFragment3(
               args.name,
               binding.nameEditText.text.toString()
            ))
        }
    }

    private fun initViews(){
        binding.enterPlaceNameTextView.text = "Укажите адрес"
    }
}