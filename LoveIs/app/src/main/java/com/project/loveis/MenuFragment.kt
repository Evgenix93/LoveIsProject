package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentMenuBinding

class MenuFragment: Fragment(R.layout.fragment_menu) {
    private val binding: FragmentMenuBinding by viewBinding()
    private val arguments: MenuFragmentArgs by navArgs()

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
                if(arguments.subscription.not())
                   findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToPremiumFragment())
                else
                   Toast.makeText(requireContext(),"Вы уже подписаны", Toast.LENGTH_SHORT).show()
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