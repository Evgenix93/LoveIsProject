package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentCreateLoveisMan7Binding
import com.project.loveis.models.User
import com.project.loveis.util.Gender
import com.project.loveis.viewmodels.CreateLoveIsEventIsViewModel

class CreateLoveIsManFragment5 : Fragment(R.layout.fragment_create_loveis_man_7) {
    private val binding: FragmentCreateLoveisMan7Binding by viewBinding()
    private val args: CreateLoveIsManFragment5Args by navArgs()
    private val viewModel: CreateLoveIsEventIsViewModel by viewModels()
    private var user: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initContinueButton()
        bindViewModel()
        getCurrentUser()
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
                is State.LoadedCurrentUser -> {
                    user = state.user
                    if(state.user.gender.name == "female")
                        setViewFemale()
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
                is State.ErrorMessageState -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                else -> {}
            }
        }
    }

    private fun initContinueButton() {
        binding.continueBtn.setOnClickListener {
            createLoveIs()
        }
    }

    private fun showLoading(loading: Boolean){
        binding.continueBtn.isEnabled = !loading
        binding.progressBar.isVisible = loading
    }

    private fun getCurrentUser(){
        viewModel.getCurrentUser()
    }

    private fun createLoveIs(){
       if (user!!.wallet?.value ?:0  < 300 && user!!.gender.name == "female") {
            Toast.makeText(requireContext(), "Недостаточно средств", Toast.LENGTH_LONG)
                .show()
            findNavController().navigate(CreateLoveIsManFragment5Directions.actionCreateLoveIsManFragment5ToAddMoneyFragment())
        }else
        viewModel.createLoveIs(
            args.type,
            args.place,
            args.date,
            args.telegramUrl,
            args.whatsappUrl,
            args.userId
        )
    }

    private fun setViewFemale(){
       binding.infoTextView.text = getString(R.string.get_loveis_cashback)
       binding.infoWithdrawalToCard.isVisible = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearState()
    }
}