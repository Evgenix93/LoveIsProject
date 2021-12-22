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
import com.project.loveis.databinding.FragmentChangePhoneNumber1Binding
import com.project.loveis.databinding.FragmentChangePhoneNumber2Binding
import com.project.loveis.viewmodels.PhoneNumberViewModel

class ChangePhoneNumber2Fragment : Fragment(R.layout.fragment_change_phone_number2) {
    private val binding: FragmentChangePhoneNumber2Binding by viewBinding()
    private val viewModel: PhoneNumberViewModel by viewModels()
    private val args: ChangePhoneNumber2FragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        setClickListeners()
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

    private fun setClickListeners() {
        binding.changeNumberButton.setOnClickListener {
            viewModel.performPhoneCheck(args.phone, binding.enterCodeEditText.rawText)
        }
    }

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when(state){
                is State.SuccessState -> findNavController().navigate(ChangePhoneNumber2FragmentDirections.actionChangePhoneNumber2FragmentToProfileFragment())
                is State.LoadingState -> showLoading(true)
                is State.ErrorState -> {
                    showLoading(false)
                    when (state.code) {
                        409 -> (requireActivity() as MainActivity).showErrorNotification()
                        410 -> (requireActivity() as MainActivity).showErrorNotification()
                        0 -> findNavController().navigate(R.id.errorFragment)
                        2 -> findNavController().navigate(R.id.serverErrorFragment)
                    }
                }
            }


        })
    }

    private fun showLoading(loading: Boolean) {
        binding.changeNumberButton.isEnabled = !loading
        binding.progressBar.isVisible = loading
    }
}