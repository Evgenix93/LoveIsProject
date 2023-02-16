package com.project.loveis

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.adapters.DialogAdapter
import com.project.loveis.adapters.UsersViewPagerAdapter
import com.project.loveis.databinding.FragmentDialogsBinding
import com.project.loveis.models.DialogsWrapper
import com.project.loveis.models.User
import com.project.loveis.util.autoCleared
import com.project.loveis.viewmodels.ChatViewModel

class DialogsFragment: Fragment(R.layout.fragment_dialogs) {
    private val binding: FragmentDialogsBinding by viewBinding()
    private var dialogAdapter: DialogAdapter by autoCleared()
    private val viewModel: ChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initList()
        (requireActivity() as MainActivity).hideBottomNavigationBar(false)
        observeState()
        setCurrentUser()
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
        dialogAdapter = DialogAdapter ({
            findNavController().navigate(DialogsFragmentDirections.actionDialogsFragmentToChatFragment(it.id!!, it.name))
        }, {
            findNavController().navigate(R.id.userFragment, Bundle().apply{
                putBoolean(UsersViewPagerAdapter.IS_LIST, false)
                putParcelable(UsersViewPagerAdapter.USER, viewModel.getCurrentUser())
                putParcelable(UsersViewPagerAdapter.USERS, it)
            })
        })
        with(binding.dialogsList){
            adapter = dialogAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun getDialogs(){
        viewModel.getDialogs()
    }

    private fun setCurrentUser(){
        viewModel.setCurrentUser()
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.LoadedSingleState -> {
                    val dialogsWrapper = state.result as DialogsWrapper
                    dialogAdapter.updateList(dialogsWrapper.list.sortedByDescending { it.lastMessage?.timestamp })
                }
                is State.ErrorMessageState -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}