package com.project.loveis

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.project.loveis.databinding.FragmentAddPlace1Binding
import com.project.loveis.databinding.FragmentAddPlace2Binding
import com.project.loveis.viewmodels.CreatePlaceViewModel
import java.io.File

class CreatePlaceFragment3: Fragment(R.layout.fragment_add_place2) {
    private val binding: FragmentAddPlace2Binding by viewBinding()
    private var isFromCreateEvent = true
    private val args: CreatePlaceFragment3Args by navArgs()
    private val viewModel: CreatePlaceViewModel by viewModels()
    private lateinit var openDocumentLauncher: ActivityResultLauncher<Array<String>>

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initIsFromCreateEvent()
        initOpenDocumentLauncher()
        initPlaceCardView()
        initContinueButton()
        bindViewModel()
    }

    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.create_place)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun initIsFromCreateEvent(){
        isFromCreateEvent = findNavController().backStack.elementAt(findNavController().backStack.size - 4).destination.id == R.id.createEventIsFragment4
    }

    private fun initPlaceCardView(){
        binding.placeCardView.setOnClickListener{
            openDocumentLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun initOpenDocumentLauncher() {
        openDocumentLauncher = registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) {uri ->
            Glide.with(binding.root)
                .load(uri)
                .into(binding.placePhotoImageView)
            viewModel.safePhotoUri(uri)
        }
    }

    private fun initContinueButton(){
        binding.continueBtn.setOnClickListener {
            viewModel.createPlace(args.name, args.adress)

        }
    }

    private fun bindViewModel(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.LoadingState -> {showLoading(true)}
                is State.LoadedSingleState -> {
                }
                is State.SuccessState -> {
                    (requireActivity() as MainActivity).showSuccessNotification()
                    if(isFromCreateEvent) {
                    findNavController().navigate(CreatePlaceFragment3Directions.actionCreatePlaceFragment3ToCreateEventIsFragment4())
                }else{
                    findNavController().navigate(CreatePlaceFragment3Directions.actionCreatePlaceFragment3ToCreateLoveIsFragment2(1, 1))
                }}
                is State.ErrorState -> {
                    showLoading(false)
                    (requireActivity() as MainActivity).showErrorNotification()
                }
                else -> {}
            }
        }
    }
    private fun showLoading(loading: Boolean) {
        binding.continueBtn.isEnabled = !loading
        binding.progressBar.isVisible = loading
    }

}