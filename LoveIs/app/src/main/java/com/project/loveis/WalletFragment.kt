package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentWalletBinding

class WalletFragment: Fragment(R.layout.fragment_wallet) {
    private val binding: FragmentWalletBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigationBar()
        initToolbar()
        initChips()
        initViewPager()
    }

    private fun initToolbar(){
        with(binding.toolbar) {
            title.text = getString(R.string.wallet)
            logOut.isVisible = false
            logOut.setOnClickListener{
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun showBottomNavigationBar(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(false)
    }

    private fun initViewPager(){
        binding.walletViewPager.adapter = WalletAdapter(this)
        binding.walletViewPager.isUserInputEnabled = false
    }

    private fun initChips(){
       binding.chipGroup.setOnCheckedChangeListener { _, checkedId ->
           when (checkedId) {
               R.id.balanceChip -> {
                   binding.balanceChip.setChipBackgroundColorResource(R.color.white)
                   binding.balanceChip.elevation = 3f
                   binding.historyChip.setChipBackgroundColorResource(R.color.chips_background)
                   binding.historyChip.elevation = 0f
                   binding.walletViewPager.setCurrentItem(0, true)
               }
               R.id.historyChip -> {
                   binding.historyChip.setChipBackgroundColorResource(R.color.white)
                   binding.historyChip.elevation = 3f
                   binding.balanceChip.setChipBackgroundColorResource(R.color.chips_background)
                   binding.balanceChip.elevation = 0f
                   binding.walletViewPager.setCurrentItem(1, true)
               }
           }
       }

    }
}