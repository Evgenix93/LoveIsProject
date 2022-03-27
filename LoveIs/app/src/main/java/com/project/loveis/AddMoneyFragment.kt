package com.project.loveis

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentAddMoneyBinding
import com.project.loveis.viewmodels.WalletViewModel

class AddMoneyFragment: Fragment(R.layout.fragment_add_money) {
    private val binding: FragmentAddMoneyBinding by viewBinding()
    private val viewModel: WalletViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        hideBottomNavigation()
        observeState()
        initEditText()
        setClickListeners()
    }

    private fun hideBottomNavigation(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(true)
    }

    private fun initToolbar(){
        with(binding.toolbar) {
            title.text = getString(R.string.add_money)
            logOut.setImageResource(R.drawable.ic_arrow_back)
            logOut.setOnClickListener{
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun setClickListeners(){
        binding.addButton.setOnClickListener{
            addMoney()
        }
        binding.clearImageView.setOnClickListener {
            binding.cardNumberEditText.text?.clear()
        }
    }

    private fun addMoney(){
      binding.loadingProgressBar.isVisible = true
      viewModel.addMoney(binding.amountEditText.text.toString().toInt())
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.SuccessState -> {
                    binding.loadingProgressBar.isVisible = false
                    Toast.makeText(requireContext(), "Кошелек пополнен", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
                is State.ErrorState -> {
                    binding.loadingProgressBar.isVisible = false
                    Toast.makeText(requireContext(), "Ошибка ${state.code}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initEditText(){
        binding.cardNumberEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if(i == EditorInfo.IME_ACTION_DONE){
                (requireActivity() as MainActivity).hideKeyboard()
                true
            }
            else
                false
        }

        binding.expireDateEditText.setOnEditorActionListener{_, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE){
                (requireActivity() as MainActivity).hideKeyboard()
                true
            }
            else
                false
        }

        binding.cvcEditText.setOnEditorActionListener{_, i, _ ->
            if(i == EditorInfo.IME_ACTION_DONE){
                (requireActivity() as MainActivity).hideKeyboard()
                true
            }
            else
                false
        }
    }
}