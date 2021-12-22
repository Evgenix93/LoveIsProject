package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentAddMoneyBinding

class AddMoneyFragment: Fragment(R.layout.fragment_add_money) {
    private val binding: FragmentAddMoneyBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        hideBottomNavigation()
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
}