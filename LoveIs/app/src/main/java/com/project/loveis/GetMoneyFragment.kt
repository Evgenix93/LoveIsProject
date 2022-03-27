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
import com.project.loveis.databinding.FragmentGetMoneyBinding
import com.project.loveis.viewmodels.WalletViewModel

class GetMoneyFragment: Fragment(R.layout.fragment_get_money) {
    private val binding: FragmentGetMoneyBinding by viewBinding()
    private val viewModel: WalletViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        hideBottomNavigation()
        initEditText()
        observeState()
        setClickListeners()
    }

    private fun hideBottomNavigation(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(true)
    }

    private fun initToolbar(){
        with(binding.toolbar) {
            title.text = getString(R.string.get_money)
            logOut.setImageResource(R.drawable.ic_arrow_back)
            logOut.setOnClickListener{
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun setClickListeners(){
        binding.getMoneyButton.setOnClickListener{
            if(binding.cardNumberEditText.rawText.length == 16)
            getMoney()
            else
                Toast.makeText(requireContext(), "Введите номер карты", Toast.LENGTH_LONG).show()
        }
        binding.clearImageView.setOnClickListener {
            binding.cardNumberEditText.text?.clear()
        }
    }

    private fun getMoney(){
        binding.loadingProgressBar.isVisible = true
       viewModel.getMoney(
           binding.amountEditText.text.toString().toInt(),
           binding.cardNumberEditText.text.toString()
       )
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.SuccessState -> {
                    binding.loadingProgressBar.isVisible = false
                    Toast.makeText(requireContext(), "Средства выведены успешно", Toast.LENGTH_LONG).show()
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
    }
}