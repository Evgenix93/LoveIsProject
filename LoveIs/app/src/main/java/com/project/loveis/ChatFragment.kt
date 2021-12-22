package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentChatBinding

class ChatFragment: Fragment(R.layout.fragment_chat) {
    private val binding: FragmentChatBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
    }



    private fun initToolbar(){
        with(binding.toolbar){
            title.text = "Jane Cooper"
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener { findNavController().popBackStack() }
            burgerMenu.isVisible = false
        }
    }
}