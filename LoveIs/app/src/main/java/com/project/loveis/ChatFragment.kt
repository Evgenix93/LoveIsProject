package com.project.loveis

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.adapters.MessageAdapter
import com.project.loveis.databinding.FragmentChatBinding
import com.project.loveis.models.Dialog
import com.project.loveis.util.autoCleared
import com.project.loveis.viewmodels.ChatViewModel

class ChatFragment: Fragment(R.layout.fragment_chat) {
    private val binding: FragmentChatBinding by viewBinding()
    private var messageAdapter: MessageAdapter by autoCleared()
    private val args: ChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
        initList()
        setClickListeners()
        observeState()
        initFilePickerLauncher()
        getMessages()
    }

    private fun initList(){
        messageAdapter = MessageAdapter(requireContext(), args.userId)
        with(binding.messageList){
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        binding.messageList.rootView.setOnClickListener { Log.d("mylog", "click") }

    }

    private fun initToolbar(){
        with(binding.toolbar){
            title.text = args.name
            logOut.setImageResource(R.drawable.ic_left_arrow)
            logOut.setOnClickListener { findNavController().popBackStack() }
            burgerMenu.isVisible = false
        }
    }

    private fun setClickListeners(){
        binding.sendImageView.setOnClickListener {
          viewModel.sendMessage(binding.messageEditText.text.toString(), args.userId)
          binding.messageEditText.text.clear()
        }
        binding.attachFileImageView.setOnClickListener {
            filePickerLauncher.launch(arrayOf("image/*"))
        }
    }

    private fun observeState(){
        viewModel.state.observe(viewLifecycleOwner){state ->
            when(state){
                is State.SuccessState -> {
                    Toast.makeText(requireContext(), "Сообщение отправлено", Toast.LENGTH_LONG).show()
                    getMessages()
                }
                is State.LoadedSingleState -> {
                    binding.loadingProgressBar.isVisible = false
                    val dialog = state.result as Dialog
                    messageAdapter.updateList(dialog.list!!.reversed())
                    if(dialog.list.isNotEmpty())
                    binding.messageList.smoothScrollToPosition(dialog.list.lastIndex)
                }
                is State.LoadingState -> {
                    binding.loadingProgressBar.isVisible = true
                }
                is State.ErrorState -> {
                    Toast.makeText(requireContext(), "error ${state.code}", Toast.LENGTH_LONG).show()
                    binding.loadingProgressBar.isVisible = false
                }
            }
        }
    }

    private fun getMessages(){
      viewModel.getMessages(args.userId)
    }

    private fun initFilePickerLauncher(){
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){ uri ->
            if(uri != null)
            viewModel.setAttachmentUri(uri)
        }
    }
}