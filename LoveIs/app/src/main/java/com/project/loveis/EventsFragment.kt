package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
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
import com.project.loveis.models.MeetingType
import com.project.loveis.util.MeetingTypeEnum
import com.project.loveis.viewmodels.LoveIsEveintIsViewModel

class EventsFragment: Fragment(R.layout.fragment_events) {
    private val binding: FragmentEventsBinding by viewBinding()
    private val viewModel: LoveIsEveintIsViewModel by viewModels()
    private val eventAdapter = LoveIsEventIsAdapter(onClick = {}, onEventClick = { eventIs ->
        findNavController().navigate(EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment(eventIs))

    },
    onAccept = {},
    onDecline = {})
    private val finishedEventIsAdapter = FinishedLoveIsAdapter()




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        initChips()
        initFilterWindow()
        showBottomNavigation()
        bindViewModel()

        binding.addEventFloatingBtn.setOnClickListener {
            findNavController().navigate(EventsFragmentDirections.actionEventsFragmentToCreateEventIsFragment1())

        }

        view.setOnClickListener {
            binding.filterWindow.root.isVisible = false
        }
    }



    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.event_is)
            logOut.isVisible = false
            burgerMenu.setImageResource(R.drawable.ic_filter)
            burgerMenu.setOnClickListener {
                binding.filterWindow.root.isVisible = binding.filterWindow.root.isVisible.not()
            }

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
                R.id.activeChip ->  {
                    binding.eventsList.adapter = eventAdapter
                    viewModel.getEventIsMeetings(type = MeetingFilterType.ACTIVE)}
                R.id.historyChip -> {
                    binding.eventsList.adapter = finishedEventIsAdapter
                    viewModel.getEventIsMeetings(type = MeetingFilterType.HISTORY)}
                R.id.allChip -> {
                    binding.eventsList.adapter = eventAdapter
                    viewModel.getEventIsMeetings(type = MeetingFilterType.ALL)}

            }
        }
    }

    private fun initFilterWindow(){
        binding.filterWindow.filterTypeTextView.setAdapter(ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            arrayOf("Все", "Прогулка", "Лыжи, сноуборды, коньки", "Поездка", "Суши", "Кафе")
        ))
        binding.filterWindow.filterTypeTextView.setOnClickListener {
            binding.filterWindow.filterTypeTextView.showDropDown()
        }
        binding.filterWindow.filterTypeTextView.setOnItemClickListener { adapterView, view, i, l ->
            when(i){
                0 -> {
                    eventAdapter.setEventTypeId(MeetingTypeEnum.ALL.id)
                    finishedEventIsAdapter.setEventTypeId(MeetingTypeEnum.ALL.id)
                    binding.filterWindow.root.isVisible = false
                }
                1 -> {
                    eventAdapter.setEventTypeId(MeetingTypeEnum.WALKING.id)
                    finishedEventIsAdapter.setEventTypeId(MeetingTypeEnum.WALKING.id)
                    binding.filterWindow.root.isVisible = false

                }
                2 -> {
                    eventAdapter.setEventTypeId(MeetingTypeEnum.SKI.id)
                    finishedEventIsAdapter.setEventTypeId(MeetingTypeEnum.SKI.id)
                    binding.filterWindow.root.isVisible = false
                }
                3 -> {
                    eventAdapter.setEventTypeId(MeetingTypeEnum.TRIP.id)
                    finishedEventIsAdapter.setEventTypeId(MeetingTypeEnum.TRIP.id)
                    binding.filterWindow.root.isVisible = false
                }
                4 -> {
                    eventAdapter.setEventTypeId(MeetingTypeEnum.SUSHI.id)
                    finishedEventIsAdapter.setEventTypeId(MeetingTypeEnum.SUSHI.id)
                    binding.filterWindow.root.isVisible = false
                }
                5 -> {
                    eventAdapter.setEventTypeId(MeetingTypeEnum.CAFE.id)
                    finishedEventIsAdapter.setEventTypeId(MeetingTypeEnum.CAFE.id)
                    binding.filterWindow.root.isVisible = false
                }

            }
        }

    }

    private fun showBottomNavigation(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(false)
    }

    private fun bindViewModel(){
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is State.StartState -> {
                    viewModel.getEventIsMeetings(type = MeetingFilterType.ALL )
                }
                is State.LoadingState -> {}
                is State.EventIsMeetingsLoadedState -> {
                    if(state.type != MeetingFilterType.HISTORY)
                    eventAdapter.updateEventIsList(state.meetings, state.type)
                    else
                        finishedEventIsAdapter.updateEventIsList(state.meetings)
                }
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