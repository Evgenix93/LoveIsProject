package com.project.loveis

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.project.loveis.databinding.FragmentPhoneNumber1Binding
import com.project.loveis.viewmodels.PhoneNumberViewModel


class PhoneNumber1Fragment : Fragment(R.layout.fragment_phone_number1) {

    private val binding: FragmentPhoneNumber1Binding by viewBinding()
    private val viewModel: PhoneNumberViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigation()
        setOnClickListener()
        bindViewModel()

    }

    private fun setOnClickListener() {
        binding.continueBtn.setOnClickListener {
            Log.d("debug", binding.phoneNumberEditText.text.toString())
            viewModel.getPhoneCode(binding.phoneNumberEditText.text.toString())
        }
    }

    private fun hideBottomNavigation() {
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(true)
    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.StartState -> {
                }
                is State.LoadingState -> {
                    showLoading(true)
                }
                is State.SuccessState -> {
                    showLoading(false)
                    findNavController().navigate(
                    PhoneNumber1FragmentDirections.actionPhoneNumber1FragmentToPhoneNumber2Fragment(
                        binding.phoneNumberEditText.text.toString()
                    )
                )}
                is State.ErrorState -> {
                    showLoading(false)
                    when (state.code) {
                        404 -> findNavController().navigate(
                            PhoneNumber1FragmentDirections.actionPhoneNumber1FragmentToRegistration1Fragment(
                                binding.phoneNumberEditText.text.toString()
                            )
                        )
                        400 -> { (requireActivity() as MainActivity).showErrorNotification() }
                        408 -> {(requireActivity() as MainActivity).showErrorNotification()}
                        409 -> {(requireActivity() as MainActivity).showErrorNotification()}
                        0 -> findNavController().navigate(R.id.errorFragment)
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