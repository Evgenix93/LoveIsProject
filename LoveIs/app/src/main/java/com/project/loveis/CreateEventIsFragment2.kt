package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateEventis2Binding
import com.project.loveis.databinding.FragmentCreateLoveis1Binding

class CreateEventIsFragment2: Fragment(R.layout.fragment_create_eventis_2) {
    private val binding: FragmentCreateEventis2Binding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(CreateEventIsFragment2Directions.actionCreateEventIsFragment2ToCreateEventIsFragment3())
        }
    }

    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.create_event_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }
}