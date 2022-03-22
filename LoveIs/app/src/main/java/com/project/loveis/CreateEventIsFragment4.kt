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
import com.project.loveis.util.AutoClearedValue
import com.project.loveis.util.autoCleared
import com.project.loveis.viewmodels.CreateLoveIsViewModel

class CreateEventIsFragment4: Fragment(R.layout.fragment_create_loveis_eventis_4) {
    private val binding: FragmentCreateLoveisEventis4Binding by viewBinding()
    private var placeAdapter: PlaceAdapter by autoCleared()
    private var place = 1
    private val viewModel: CreateLoveIsViewModel by viewModels()
    private val args: CreateEventIsFragment4Args by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        bindViewModel()
        initAddPlaceText()
        initContinueButton()
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
        placeAdapter = PlaceAdapter { place = it }
        with(binding.placesList) {
            adapter = placeAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initAddPlaceText() {
        binding.addPlaceTextView.setOnClickListener {
            findNavController().navigate(CreateEventIsFragment4Directions.actionCreateEventIsFragment4ToCreatePlaceFragment1())
        }
    }

    private fun bindViewModel(){
        viewModel.clearState()
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.StartState -> viewModel.getPlaces()
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
                CreateEventIsFragment4Directions.actionCreateEventIsFragment4ToCreateEventIsFragment5(
                  args.type,
                  args.personCount,
                  args.price,
                  place
                )
            )
        }
    }
}