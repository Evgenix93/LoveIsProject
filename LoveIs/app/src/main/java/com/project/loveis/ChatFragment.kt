package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.adapters.MessageAdapter
import com.project.loveis.databinding.FragmentChatBinding

class ChatFragment: Fragment(R.layout.fragment_chat) {
    private val binding: FragmentChatBinding by viewBinding()
    private var messageAdapter: MessageAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
        initList()
    }

    private fun initList(){
        messageAdapter = MessageAdapter(requireContext())
        with(binding.messageList){
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        messageAdapter?.notifyDataSetChanged()
        binding.messageList.rootView.setOnClickListener { Log.d("mylog", "click") }

    }



    private fun initToolbar(){
        with(binding.toolbar){
            title.text = "Jane Cooper"
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener { findNavController().popBackStack() }
            burgerMenu.isVisible = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        messageAdapter = null
    }
}