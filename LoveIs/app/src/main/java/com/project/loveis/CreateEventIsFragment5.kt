package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateLoveisEventis5Binding

class CreateEventIsFragment5 : Fragment(R.layout.fragment_create_loveis_eventis_5) {
    private val binding: FragmentCreateLoveisEventis5Binding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(CreateEventIsFragment5Directions.actionCreateEventIsFragment5ToCreateEventIsFragment6())
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