package com.project.loveis

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.allViews
import androidx.core.view.descendants
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.project.loveis.databinding.FragmentLoveIsBinding
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.util.MeetingStatus
import com.project.loveis.viewmodels.LoveIsViewModel

class LoveIsFragment : Fragment(R.layout.fragment_love_is) {
    private val binding: FragmentLoveIsBinding by viewBinding()
    private val viewModel: LoveIsViewModel by viewModels()
    private val loveIsAdapter = LoveIsEventIsAdapter(onClick = { loveIs ->
        findNavController().navigate(
            LoveIsFragmentDirections.actionLoveIsFragmentToLoveIsDetailsFragment(
                loveIs,
                currentFilterType.value
            )
        )
    },
        onAccept = { loveIsId ->
            viewModel.acceptLoveIs(loveIsId)
        },
    onDecline = { loveIsId ->
        viewModel.changeLoveIsStatus(loveIsId, MeetingStatus.CANCEL)

    })
    private val finishedLoveIsAdapter = FinishedLoveIsAdapter()
    private var currentFilterType = MeetingFilterType.ACTIVE


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        initChips()
        (requireActivity() as MainActivity).hideBottomNavigationBar(false)
        bindViewModel()


    }


    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(R.string.love_is)
            logOut.isVisible = false
            burgerMenu.isVisible = false
        }

    }

    private fun initList() {
        with(binding.list) {
            adapter = loveIsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)

        }
    }

    private fun initChips() {
        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            Log.d("debug", "checked")
            group.descendants.forEach {
                val chip = it as Chip
                if (chip.isChecked) {
                    chip.setChipBackgroundColorResource(R.color.white)
                    chip.elevation = 3f

                } else {
                    chip.setChipBackgroundColorResource(R.color.chips_background)
                    chip.elevation = 0f
                }
            }

            when (checkedId) {
                R.id.activeChip -> {
                    binding.list.adapter = loveIsAdapter
                    viewModel.getLoveIsMeetings(type = MeetingFilterType.ACTIVE)
                }
                R.id.myChip -> {
                    binding.list.adapter = loveIsAdapter
                    viewModel.getLoveIsMeetings(type = MeetingFilterType.MY)
                }
                R.id.newChip -> {
                    binding.list.adapter = loveIsAdapter
                    viewModel.getLoveIsMeetings(type = MeetingFilterType.INCOMING)
                }
                R.id.historyChip -> {
                    binding.list.adapter = finishedLoveIsAdapter
                    viewModel.getLoveIsMeetings(type = MeetingFilterType.HISTORY)
                }

            }
        }
    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.StartState -> viewModel.getLoveIsMeetings(type = MeetingFilterType.ACTIVE)
                is State.LoadingState -> {}
                is State.LoveIsMeetingsLoadedState -> {
                    currentFilterType = state.type
                    if (state.type != MeetingFilterType.HISTORY)
                        loveIsAdapter.updateList(state.meetings, state.type)
                    else
                        finishedLoveIsAdapter.updateList(state.meetings)
                }
                is State.SuccessState -> {
                    viewModel.getLoveIsMeetings(type = MeetingFilterType.INCOMING)
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