package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateLoveisEventis4Binding
import com.project.loveis.util.AutoClearedValue
import com.project.loveis.util.autoCleared

class CreateEventIsFragment4: Fragment(R.layout.fragment_create_loveis_eventis_4) {
    private val binding: FragmentCreateLoveisEventis4Binding by viewBinding()
    private var placeAdapter: PlaceAdapter by autoCleared()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        binding.addPlaceTextView.setOnClickListener {
            findNavController().navigate(CreateEventIsFragment4Directions.actionCreateEventIsFragment4ToCreatePlaceFragment1())
        }
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(CreateEventIsFragment4Directions.actionCreateEventIsFragment4ToCreateEventIsFragment5())
        }
    }

    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.create_event_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack(R.id.createEventIsFragment3, false)

            }
            burgerMenu.isVisible = false
        }
    }

    private fun initList(){
        placeAdapter = PlaceAdapter{

        }
        with(binding.placesList){
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
}