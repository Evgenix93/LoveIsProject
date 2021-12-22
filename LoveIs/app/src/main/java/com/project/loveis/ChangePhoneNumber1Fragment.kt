package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentChangePhoneNumber1Binding
import com.project.loveis.viewmodels.PhoneNumberViewModel


class ChangePhoneNumber1Fragment : Fragment(R.layout.fragment_change_phone_number1) {

    private val binding: FragmentChangePhoneNumber1Binding by viewBinding()
    private val viewModel: PhoneNumberViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setOnClickListener()
        bindViewModel()
    }

    private fun initToolbar() {
        with(binding.toolbar) {
            title.text = getString(R.string.change_number)
            logOut.setImageResource(R.drawable.ic_arrow_back)
            logOut.setOnClickListener {
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun setOnClickListener() {
        binding.continueBtn.setOnClickListener {
            viewModel.changePhone(binding.phoneNumberEditText.text.toString())

        }

    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.SuccessState -> {
                    showLoading(false)
                    findNavController().navigate(
                    ChangePhoneNumber1FragmentDirections.actionChangePhoneNumber1FragmentToChangePhoneNumber2Fragment(
                        binding.phoneNumberEditText.text.toString()
                    )
                )}
                is State.LoadingState -> showLoading(true)
                is State.ErrorState -> {
                    showLoading(false)
                    when (state.code) {
                        400 -> (requireActivity() as MainActivity).showErrorNotification()
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