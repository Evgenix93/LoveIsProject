package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentLoveisEventisDetailsBinding


class EventDetailsFragment: Fragment(R.layout.fragment_loveis_eventis_details) {
    private val binding: FragmentLoveisEventisDetailsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        bind()
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
    }


    private fun bind(){
        binding.placeImage.setImageResource(R.drawable.cafe)
        binding.placeNameTextView.text = "Smile"
        binding.placeAddressTextView.text = "Санкт Петербург"
        binding.dateTextView.text = "22.10.21"
        binding.timeTextView.text = "22:30"
        binding.loveIsPersonsTextView.text = "Участники встречи (1/3)"
        binding.personCountTextView.text = "3"
        binding.personQuantityTextView.text = " / 10"

    }

    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(com.project.loveis.R.string.event_is)
            logOut.setImageResource(com.project.loveis.R.drawable.ic_left_arrow)
            logOut.setOnClickListener { findNavController().popBackStack() }
            burgerMenu.setImageResource(com.project.loveis.R.drawable.ic_share)
        }
    }
}