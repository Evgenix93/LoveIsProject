package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentWalletBalanceBinding

class WalletBalanceFragment: Fragment(R.layout.fragment_wallet_balance) {
    private val binding: FragmentWalletBalanceBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
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
}