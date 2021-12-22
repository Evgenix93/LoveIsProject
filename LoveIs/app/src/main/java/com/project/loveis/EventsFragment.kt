package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.descendants
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.project.loveis.databinding.FragmentEventsBinding

class EventsFragment: Fragment(R.layout.fragment_events) {
    private val binding: FragmentEventsBinding by viewBinding()
    private val eventAdapter = LoveIsEventIsAdapter{
        findNavController().navigate(EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        initChips()
        showBottomNavigation()

        binding.addEventFloatingBtn.setOnClickListener {
            findNavController().navigate(EventsFragmentDirections.actionEventsFragmentToCreateEventIsFragment1())

        }
    }



    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.event_is)
            logOut.isVisible = false
            burgerMenu.setImageResource(R.drawable.ic_filter)
        }

    }

    private fun initList(){
        with(binding.eventsList){
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
    }

    private fun initChips(){
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            Log.d("debug", "checked")
            group.descendants.forEach {
                val chip = it as Chip
                if(chip.isChecked){
                    chip.setChipBackgroundColorResource(R.color.white)
                    chip.elevation = 3f

                }else{
                    chip.setChipBackgroundColorResource(R.color.chips_background)
                    chip.elevation = 0f
                }
            }

            when(checkedId){
                R.id.activeChip ->  {}
                R.id.myChip -> {}
                R.id.newChip -> {}
                R.id.historyChip -> {}

            }
        }
    }

    private fun showBottomNavigation(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(false)
    }
}