package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.descendants
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.project.loveis.databinding.FragmentEventsBinding
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.viewmodels.LoveIsEveintIsViewModel

class EventsFragment: Fragment(R.layout.fragment_events) {
    private val binding: FragmentEventsBinding by viewBinding()
    private val viewModel: LoveIsEveintIsViewModel by viewModels()
    private val eventAdapter = LoveIsEventIsAdapter(onClick = {
        findNavController().navigate(EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment())

    },
    onAccept = {},
    onDecline = {})




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        initChips()
        showBottomNavigation()
        bindViewModel()

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
                R.id.activeChip ->  {viewModel.getEventIsMeetings(type = MeetingFilterType.ACTIVE)}
                R.id.historyChip -> {viewModel.getEventIsMeetings(type = MeetingFilterType.HISTORY)}
                R.id.allChip -> {viewModel.getEventIsMeetings(type = MeetingFilterType.ALL)}

            }
        }
    }

    private fun showBottomNavigation(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(false)
    }

    private fun bindViewModel(){
        Log.d("debug", "bindViewModel")
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is State.StartState -> {
                    Log.d("mylog", "onViewCreated")
                    viewModel.getEventIsMeetings(type = MeetingFilterType.ALL )
                }
                is State.LoadingState -> {}
                is State.EventIsMeetingsLoadedState -> eventAdapter.updateEventIsList(state.meetings, state.type)
                is State.ErrorState -> {
                    when (state.code) {
                        400 -> showToast("Ошибка")
                        0 -> findNavController().navigate(R.id.errorFragment)
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }

                }
            }


        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}