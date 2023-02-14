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
import com.project.loveis.databinding.FragmentCreateLoveis1Binding
import com.project.loveis.databinding.FragmentCreateLoveisEventis6Binding
import com.project.loveis.viewmodels.CreateLoveIsEventIsViewModel

class CreateLoveIsFragment4 : Fragment(R.layout.fragment_create_loveis_eventis_6) {
    private val binding: FragmentCreateLoveisEventis6Binding by viewBinding()
    private val args: CreateLoveIsFragment4Args by navArgs()
    private val viewModel: CreateLoveIsEventIsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initSteps()
        initContinueButton()
        bindViewModel()
    }


    private fun initSteps(){
        binding.stepTextView2.text = "4"
        binding.stepTextView4.text = "4"
    }

    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = requireContext().getString(R.string.create_love_is)
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun initContinueButton() {
        binding.continueBtn.setOnClickListener {
            val telegramUrl = binding.telegramRefEditText.text.toString()
            val whatsappUrl = binding.whatsAppRefEditText.text.toString()
            viewModel.createLoveIs(
                args.type,
                args.place,
                args.date,
                telegramUrl,
                whatsappUrl,
                args.userId
            )
            /*val telegramUrl = binding.telegramRefEditText.text.toString()
            val whatsappUrl = binding.whatsAppRefEditText.text.toString()
            findNavController().navigate(CreateLoveIsFragment4Directions.actionCreateLoveIsFragment4ToCreateLoveIsManFragment5(
              args.type,
              args.place,
              args.date,
              telegramUrl,
              whatsappUrl,
                args.userId,
            ))*/
        }
    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.LoadingState -> {showLoading(true)}
                is State.LoadedCurrentUser -> {
                    showLoading(false)
                    //user = state.user
                    //if(state.user.gender.name == Gender.female.name)
                    //setViewFemale()
                }
                is State.SuccessState -> {
                    (requireActivity() as MainActivity).showSuccessNotification()
                    findNavController().navigate(R.id.loveIsFragment)
                }
                is State.ErrorState -> {
                    showLoading(false)
                    when (state.code) {
                        400 -> (requireActivity() as MainActivity).showErrorNotification()
                        404 -> findNavController().navigate(R.id.serverErrorFragment)
                        409 -> (requireActivity() as MainActivity).showErrorNotification()
                        500 -> Toast.makeText(requireContext(), "error 500", Toast.LENGTH_LONG).show()
                        0 -> findNavController().navigate(R.id.errorFragment)

                    }
                }
                is State.ErrorMessageState -> {Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                showLoading(false)}
                else -> {}
            }
        }
    }

    private fun showLoading(loading: Boolean){
        binding.continueBtn.isEnabled = !loading
        binding.progressBar.isVisible = loading
    }

}