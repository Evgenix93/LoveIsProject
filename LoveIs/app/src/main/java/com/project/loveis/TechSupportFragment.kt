package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentTechSupportBinding

class TechSupportFragment: Fragment(R.layout.fragment_tech_support) {
    private val binding: FragmentTechSupportBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()

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
}