package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding

import com.project.loveis.databinding.FragmentCreateLoveisEventis6Binding
import com.project.loveis.viewmodels.CreateLoveIsEventIsViewModel

class CreateEventIsFragment6 : Fragment(R.layout.fragment_create_loveis_eventis_6) {
    private val binding: FragmentCreateLoveisEventis6Binding by viewBinding()
    private val args: CreateEventIsFragment6Args by navArgs()
    private val viewModelIsEvent: CreateLoveIsEventIsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initContinueButton()
        bindViewModel()
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

    private fun initContinueButton(){
        binding.continueBtn.setOnClickListener {
           viewModelIsEvent.createEventIs(
               args.type,
               args.place,
               args.date,
               args.price,
               args.personCount,
               binding.telegramRefEditText.text.toString(),
               binding.whatsAppRefEditText.text.toString()
           )
        }
    }

    private fun bindViewModel() {
        viewModelIsEvent.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.LoadingState -> {showLoading(true)}
                is State.SuccessState -> {
                    (requireActivity() as MainActivity).showSuccessNotification()
                    findNavController().navigate(CreateEventIsFragment6Directions.actionCreateEventIsFragment6ToEventsFragment())
                }
                is State.ErrorState -> {
                    showLoading(false)
                    when (state.code) {
                        400 -> {showLoading(false)
                            (requireActivity() as MainActivity).showErrorNotification()}
                        404 -> {showLoading(false)
                            findNavController().navigate(R.id.serverErrorFragment)}
                        409 -> {showLoading(false)
                            (requireActivity() as MainActivity).showErrorNotification()}
                        500 -> {showLoading(false)
                            Toast.makeText(requireContext(), "error 500", Toast.LENGTH_LONG).show()}
                        0 -> {showLoading(false)
                            findNavController().navigate(R.id.errorFragment)}
                    }
                }
                is State.ErrorMessageState -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }
    }

    private fun showLoading(loading: Boolean){
        binding.continueBtn.isEnabled = !loading
       // binding.progressBar.isVisible = loading
    }
}