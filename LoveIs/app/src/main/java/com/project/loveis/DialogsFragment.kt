package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentDialogsBinding

class DialogsFragment: Fragment(R.layout.fragment_dialogs) {
    private val binding: FragmentDialogsBinding by viewBinding()
    private val dialogAdapter = DialogAdapter {
        findNavController().navigate(DialogsFragmentDirections.actionDialogsFragmentToChatFragment())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        (requireActivity() as MainActivity).hideBottomNavigationBar(false)
    }


    private fun initToolbar(){
        with(binding.toolbar){
            title.text = requireContext().resources.getString(R.string.dialogs)
            burgerMenu.isVisible = false
            logOut.isVisible = false
        }

    }

    private fun initList(){
        with(binding.dialogsList){
            adapter = dialogAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }
}