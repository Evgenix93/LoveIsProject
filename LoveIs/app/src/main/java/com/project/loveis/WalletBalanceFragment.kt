package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentWalletBalanceBinding
import com.project.loveis.models.Wallet
import com.project.loveis.viewmodels.WalletViewModel

class WalletBalanceFragment: Fragment(R.layout.fragment_wallet_balance) {
    private val binding: FragmentWalletBalanceBinding by viewBinding()
    private val viewModel: WalletViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
        observeState()
    }

    override fun onResume() {
        super.onResume()
        getCurrentBalance()
    }

    private fun setOnClickListener(){
        with(binding){
            addMoneyCardView.setOnClickListener{
                findNavController().navigate(WalletFragmentDirections.actionWalletFragmentToAddMoneyFragment())
            }
            getMoneyCardView.setOnClickListener{
                findNavController().navigate(WalletFragmentDirections.actionWalletFragmentToGetMoneyFragment())
            }
        }
    }

    private fun getCurrentBalance(){
        viewModel.getCurrentBalance()
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
           when(state){
               is State.LoadedSingleState -> binding.amountTextView.text = (state.result as Wallet).value.toString()
               is State.ErrorState -> Toast.makeText(requireContext(), "Ошибка ${state.code}", Toast.LENGTH_LONG).show()
           }
        }
    }
}