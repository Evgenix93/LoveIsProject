package com.project.loveis

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.databinding.FragmentDialogsBinding
import com.project.loveis.models.DialogsWrapper
import com.project.loveis.viewmodels.ChatViewModel

class DialogsFragment: Fragment(R.layout.fragment_dialogs) {
    private val binding: FragmentDialogsBinding by viewBinding()
    private val dialogAdapter = DialogAdapter {
        findNavController().navigate(DialogsFragmentDirections.actionDialogsFragmentToChatFragment(it.id!!, it.name))
    }
    private val viewModel: ChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        (requireActivity() as MainActivity).hideBottomNavigationBar(false)
        observeState()
        getDialogs()
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

    private fun getDialogs(){
        viewModel.getDialogs()
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.LoadedSingleState -> {
                    val dialogsWrapper = state.result as DialogsWrapper
                    dialogAdapter.updateList(dialogsWrapper.list)
                }
            }
        }
    }
}