package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.project.loveis.databinding.FragmentPhoneNumber2Binding
import com.project.loveis.viewmodels.PhoneNumberViewModel


class PhoneNumber2Fragment: Fragment(R.layout.fragment_phone_number2) {
    private val binding: FragmentPhoneNumber2Binding by viewBinding()
    private val viewModel: PhoneNumberViewModel by viewModels()
    private val args: PhoneNumber2FragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        bindViewModel()
    }

    private fun setOnClickListener() {
        binding.continueBtn.setOnClickListener{
                viewModel.performPhoneCheck(args.phone, binding.enterCodeEditText.rawText)
        }
    }

    private fun bindViewModel(){
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is State.StartState -> {}
                is State.LoadingState -> {showLoading(true)}
                is State.SuccessState -> {
                    showLoading(false)
                    (requireActivity() as MainActivity).showSuccessNotification()
                    findNavController().navigate(PhoneNumber2FragmentDirections.actionPhoneNumber2FragmentToWelcomeFragment())
                }
                is State.ErrorState -> {
                    showLoading(false)
                    binding.continueBtn.isEnabled = true
                    when(state.code){
                        400 -> {(requireActivity() as MainActivity).showErrorNotification()}
                        410 -> {}
                        409 -> {(requireActivity() as MainActivity).showErrorNotification()}
                        401 -> {(requireActivity() as MainActivity).showErrorNotification()}
                        0 -> {findNavController().navigate(R.id.errorFragment)}
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }
                }
            }

        })
    }

    private fun showLoading(loading: Boolean) {
        binding.continueBtn.isEnabled = !loading
        binding.progressBar.isVisible = loading
    }
}