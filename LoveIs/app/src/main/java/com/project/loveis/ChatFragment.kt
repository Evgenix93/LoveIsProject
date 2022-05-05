package com.project.loveis

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.project.loveis.adapters.MessageAdapter
import com.project.loveis.databinding.FragmentChatBinding
import com.project.loveis.models.Dialog
import com.project.loveis.models.PushModel
import com.project.loveis.util.MessagingService
import com.project.loveis.util.autoCleared
import com.project.loveis.viewmodels.ChatViewModel

class ChatFragment: Fragment(R.layout.fragment_chat) {
    private val binding: FragmentChatBinding by viewBinding()
    private var messageAdapter: MessageAdapter by autoCleared()
    private val args: ChatFragmentArgs by navArgs()
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var filePickerLauncher: ActivityResultLauncher<Array<String>>
    private val messageReceiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            intent?.let {
                val push = it.getParcelableExtra<PushModel>(MessagingService.PUSH_DATA)
                push?.from ?: return
                if(args.userId == push.from ){
                    getMessages()
                }
                else{
                    (activity as MainActivity).onMessageReceived(push.from)
                }
            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        removeNotification()
        initToolbar()
        (requireActivity() as MainActivity).hideBottomNavigationBar(true)
        initList()
        setClickListeners()
        observeState()
        initFilePickerLauncher()
        getMessages()
        registerReceiver()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterReceiver()
    }

    private fun registerReceiver(){
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(messageReceiver,
        IntentFilter(MessagingService.PUSH_INTENT)
        )
    }

    private fun unregisterReceiver(){
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(messageReceiver)
    }

    private fun removeNotification(){
        NotificationManagerCompat.from(requireContext()).cancel(args.userId.toInt())
    }

    private fun initList(){
        messageAdapter = MessageAdapter(requireContext(), args.userId){ uri ->
            findNavController().navigate(R.id.userPhotoFragment, bundleOf(UserPhotoFragment.PHOTO_KEY to uri))

        }
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
            binding.photoCountTextView.isVisible = false
            binding.photoCountImageView.isVisible = false
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
                is State.ErrorMessageState -> Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getMessages(){
      viewModel.getMessages(args.userId)
    }

    private fun initFilePickerLauncher(){
        filePickerLauncher = registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()){ uris ->
            if(uris != null) {
                viewModel.setAttachmentUri(uris)
                binding.photoCountTextView.text = uris.size.toString()
                binding.photoCountTextView.isVisible = uris.size != 0
                binding.photoCountImageView.isVisible = uris.size != 0
            }else {
                binding.photoCountTextView.isVisible = false
                binding.photoCountImageView.isVisible = false
            }

        }
    }
}