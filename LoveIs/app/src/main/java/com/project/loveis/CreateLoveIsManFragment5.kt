package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateLoveis1Binding
import com.project.loveis.databinding.FragmentCreateLoveisMan7Binding
import com.project.loveis.viewmodels.CreateLoveIsViewModel

class CreateLoveIsManFragment5 : Fragment(R.layout.fragment_create_loveis_man_7) {
    private val binding: FragmentCreateLoveisMan7Binding by viewBinding()
    private val args: CreateLoveIsManFragment5Args by navArgs()
    private val viewModel: CreateLoveIsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initContinueButton()
        bindViewModel()
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

    private fun bindViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.LoadingState -> {showLoading(true)}
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
                        0 -> findNavController().navigate(R.id.errorFragment)
                    }
                }
                else -> {}
            }
        }
    }

    private fun initContinueButton() {
        binding.continueBtn.setOnClickListener {
            viewModel.createLoveIs(
                args.type,
                args.place,
                args.date,
                args.telegramUrl,
                args.whatsappUrl,
                args.userId
            )
            //findNavController().navigate(CreateLoveIsManFragment5Directions.actionCreateLoveIsManFragment5ToSearchFragment())
        }
    }

    private fun showLoading(loading: Boolean){
        binding.continueBtn.isEnabled = !loading
        binding.progressBar.isVisible = loading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearState()
    }
}