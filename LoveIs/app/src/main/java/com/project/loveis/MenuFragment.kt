package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentMenuBinding

class MenuFragment: Fragment(R.layout.fragment_menu) {
    private val binding: FragmentMenuBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        hideBottomNavigation()
        setOnClickListeners()
    }

    private fun initToolbar(){
        with(binding.toolbar) {
            title.text = getString(R.string.menu)
            logOut.setImageResource(R.drawable.ic_arrow_back)
            logOut.setOnClickListener{
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }

    private fun setOnClickListeners() {
        with(binding) {
            walletTextView.setOnClickListener {
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToWalletFragment())
            }
            videoTextView.setOnClickListener{
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToVideoFragment())
            }
            techSupportTextView.setOnClickListener{
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToTechSupportFragment())
            }
            changePhoneTextView.setOnClickListener{
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToChangePhoneNumber1Fragment())
            }
            moreFuturesTextView.setOnClickListener{
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToPremiumFragment())
            }
            aboutAppTextView.setOnClickListener{
                findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToAboutAppFragment())
            }
        }
    }

    private fun hideBottomNavigation(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(true)
    }
}