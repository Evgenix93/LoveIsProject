package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentTechSupportBinding
import com.project.loveis.viewmodels.TechSupportViewModel

class TechSupportFragment: Fragment(R.layout.fragment_tech_support) {
    private val binding: FragmentTechSupportBinding by viewBinding()
    private val viewModel: TechSupportViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        observeState()
        setClickListeners()
    }

    private fun initToolbar(){
        with(binding.toolbar) {
            title.text = getString(R.string.tech_support2)
            logOut.setImageResource(R.drawable.ic_arrow_back)
            logOut.setOnClickListener{
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.SuccessState -> {
                    binding.loadingProgressBar.isVisible = false
                    Toast.makeText(requireContext(), "Запрос успешно отправлен", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
                is State.ErrorState -> {
                    binding.loadingProgressBar.isVisible = false
                    Toast.makeText(requireContext(), "Ошибка ${state.code}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setClickListeners(){
        binding.sendRequestButton.setOnClickListener{
            binding.loadingProgressBar.isVisible = true
            viewModel.sendRequest(binding.requestContentTextView.text.toString())
        }
    }
}