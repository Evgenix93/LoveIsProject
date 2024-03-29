package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateLoveisEventis4Binding
import com.project.loveis.models.PlaceListResult
import com.project.loveis.util.autoCleared
import com.project.loveis.viewmodels.CreateLoveIsEventIsViewModel

class CreateLoveIsFragment2 : Fragment(R.layout.fragment_create_loveis_eventis_4) {
    private val binding: FragmentCreateLoveisEventis4Binding by viewBinding()
    private var placeAdapter: PlaceAdapter by autoCleared()
    private val args: CreateLoveIsFragment2Args by navArgs()
    private val viewModelIsEvent: CreateLoveIsEventIsViewModel by viewModels()
    private var place = 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initSteps()
        initList()
        initAddPlaceText()
        bindViewModel()
        initContinueButton()
        viewModelIsEvent.getPlaces()
    }

    private fun initSteps(){
        binding.stepTextView2.text = "2"
        binding.stepTextView4.text = "4"
    }
    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(R.string.create_love_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack(R.id.createLoveIsFragment1, false)
            }
            burgerMenu.isVisible = false
        }
    }

    private fun initList() {
        placeAdapter = PlaceAdapter { place = it }
        with(binding.placesList) {
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initAddPlaceText() {
        binding.addPlaceTextView.setOnClickListener {
            findNavController().navigate(CreateLoveIsFragment2Directions.actionCreateLoveIsFragment2ToCreatePlaceFragment1())
        }
    }

    private fun bindViewModel(){
        viewModelIsEvent.state.observe(viewLifecycleOwner){ state ->
            when(state){
               // is State.StartState -> viewModelIsEvent.getPlaces()
                is State.LoadedSingleState -> {
                    val placeListResult = state.result as PlaceListResult
                    placeAdapter.updateList(placeListResult.list)
                }
                is State.ErrorState -> {
                    (requireActivity() as MainActivity).showErrorNotification()
                }
            }
        }
    }

    private fun initContinueButton() {
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(
                CreateLoveIsFragment2Directions.actionCreateLoveIsFragment2ToCreateLoveIsFragment3(
                    args.type,
                    place,
                    args.userId
                )
            )
        }
    }
}