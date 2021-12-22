package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentVerify1Binding

class Verify1Fragment: Fragment(R.layout.fragment_verify1) {
    private val binding: FragmentVerify1Binding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        hideBottomNavigation()
        setOnClickListener()
    }

    private fun setOnClickListener(){
        binding.continueBtn.setOnClickListener{
            findNavController().navigate(Verify1FragmentDirections.actionVerify1FragmentToVerify2Fragment())
        }
    }

    private fun hideBottomNavigation(){
        val mainActivity = activity as MainActivity
        mainActivity.hideBottomNavigationBar(true)
    }

    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().getString(R.string.verification)
            logOut.setImageResource(R.drawable.ic_arrow_back)
            logOut.setOnClickListener{
                findNavController().popBackStack()
            }
            burgerMenu.isVisible = false
        }
    }
}