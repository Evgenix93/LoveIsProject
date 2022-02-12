package com.project.loveis

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateEventis3Binding

class CreateEventIsFragment3: Fragment(R.layout.fragment_create_eventis_3) {
    private val binding: FragmentCreateEventis3Binding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initCheckBoxes()
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(CreateEventIsFragment3Directions.actionCreateEventIsFragment3ToCreateEventIsFragment4())
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

    private fun initCheckBoxes(){
        binding.switchCompat.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
             binding.switchCompat.thumbTintList = null
              binding.switchCompat.trackTintList = null
              binding.switchCompat2.isChecked = false
            }
            else{
                binding.switchCompat.thumbTintList = ColorStateList.valueOf(resources.getColor(R.color.gray4))
                binding.switchCompat.trackTintList = ColorStateList.valueOf(resources.getColor(R.color.gray5))
            }
        }
        binding.switchCompat2.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                binding.switchCompat2.thumbTintList = null
                binding.switchCompat2.trackTintList = null
                binding.switchCompat.isChecked = false
            }
            else{
                binding.switchCompat2.thumbTintList = ColorStateList.valueOf(resources.getColor(R.color.gray4))
                binding.switchCompat2.trackTintList = ColorStateList.valueOf(resources.getColor(R.color.gray5))
            }
        }
    }
}